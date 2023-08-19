package cc.kermanispretty.config.common;

import cc.kermanispretty.config.common.annotation.Configurable;
import cc.kermanispretty.config.common.location.ConfigurablePrefix;
import cc.kermanispretty.config.common.reflection.context.CommentContext;
import cc.kermanispretty.config.common.reflection.context.FieldContext;
import cc.kermanispretty.config.common.reflection.context.LocationContext;
import cc.kermanispretty.config.common.reflection.processor.AnnotationLocationProcessor;
import cc.kermanispretty.config.common.reflection.processor.ClassHierarchyProcessor;
import cc.kermanispretty.config.common.reflection.processor.CommentContextProcessor;
import cc.kermanispretty.config.common.transform.TransformerHandler;
import cc.kermanispretty.config.common.transform.processer.TransformerProcessor;
import cc.kermanispretty.config.common.validation.ValidationHandler;
import cc.kermanispretty.config.common.validation.processer.ValidationProcessor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.stream.Stream;

public abstract class Config {
    private final ConfigHandler configHandler;
    private final ValidationHandler validationHandler;
    private final TransformerHandler transformerHandler;
    private final EnumSet<ConfigOptionEnum> options;
    private final LinkedHashSet<LocationContext> locationContexts;

    public Config(ConfigHandler configHandler, ValidationHandler validationHandler, TransformerHandler transformerHandler, EnumSet<ConfigOptionEnum> options) {
        this.configHandler = configHandler;
        this.validationHandler = validationHandler;
        this.transformerHandler = transformerHandler;
        this.options = options;
        this.locationContexts = new LinkedHashSet<>();
    }

    public void unregister(Object object) {
        locationContexts.removeIf(fieldContext -> // TODO: Dirty one line and could be buggy with nulls
                fieldContext.getOwningClass().equals(object.getClass()));
    }

    public void register(Object... objects) {
        for (Object object : objects) {
            boolean isClass = object instanceof Class<?>;
            Class<?> owningClass = isClass ? (Class<?>) object : object.getClass();
            Object instance = isClass ? null : object;

            String separator = configHandler.separator(); // Store an instance of the separator.
            LinkedHashSet<Class<?>> classHierarchy = new LinkedHashSet<>();

            ClassHierarchyProcessor.process(classHierarchy, owningClass, options);

            Stream<Class<?>> classStream = classHierarchy.stream()
                    .filter(clazz -> clazz.isAnnotationPresent(Configurable.class));

            LinkedHashSet<Class<?>> classHierarchyCopy = new LinkedHashSet<>();

            classStream.forEachOrdered(clazz -> {
                classHierarchyCopy.add(clazz); // Append it to the copy.

                String location = AnnotationLocationProcessor.process(classHierarchyCopy, separator);

                //Duplicates can happen, that is another reason we use a HashSet.
                CommentContextProcessor.process(locationContexts, clazz, location, instance, false);
            });

            //Use the previous hierarchy to process fields.
            StringBuilder prefix = new StringBuilder(AnnotationLocationProcessor.process(classHierarchy, separator));

            if (!isClass && instance instanceof ConfigurablePrefix)
                prefix.append(separator)
                        .append(((ConfigurablePrefix) instance).getConfigurablePrefix());

            Stream<Field> configurableFields = Arrays.stream(owningClass.getDeclaredFields())
                    .filter(field -> field.isAnnotationPresent(Configurable.class));

            configurableFields.forEachOrdered(field -> {
                String location = prefix + separator + AnnotationLocationProcessor.processSimpleName(field);

                FieldContext context = new FieldContext(field, location, instance);

                locationContexts.add(context);

                CommentContextProcessor.process(locationContexts, field, location, instance, true);
            });
        }
    }

    public void load() throws IllegalAccessException {
        configHandler.loadConfig();

        boolean didChange = false;

        for (LocationContext context : locationContexts) {
            if (context instanceof FieldContext) {
                FieldContext fieldContext = (FieldContext) context;
                fieldContext.push();

                Object defaultValue = fieldContext.getValue();

                if (options.contains(ConfigOptionEnum.CHECK_DEFAULT_NULL) || defaultValue != null) {
                    if (options.contains(ConfigOptionEnum.CHECK_DEFAULT_FOR_VALIDATION)) {
                        ValidationProcessor.validate(fieldContext.getField(), defaultValue, validationHandler.getValidators(), this);
                    }

                    Object transformedValue = TransformerProcessor.transform(fieldContext.getField(), defaultValue, transformerHandler.getTransformers(), this);

                    if (defaultValue != transformedValue)
                        fieldContext.set(transformedValue);

                    if (options.contains(ConfigOptionEnum.CHECK_DEFAULT_TRANSFORMED_FOR_VALIDATION) && defaultValue != transformedValue) {
                        ValidationProcessor.validate(fieldContext.getField(), transformedValue, validationHandler.getValidators(), this);
                    }
                }

                if (configHandler.exists(fieldContext.getLocation())) {
                    Object value = configHandler.get(fieldContext);

                    if (options.contains(ConfigOptionEnum.CHECK_CONFIG_FOR_VALIDATION)) {
                        ValidationProcessor.validate(fieldContext.getField(), value, validationHandler.getValidators(), this);
                    }

                    Object transformedValue = TransformerProcessor.transform(fieldContext.getField(), value, transformerHandler.getTransformers(), this);

                    if (defaultValue != transformedValue)
                        fieldContext.set(transformedValue);

                    if (options.contains(ConfigOptionEnum.CHECK_CONFIG_TRANSFORMED_FOR_VALIDATION) && value != transformedValue) {
                        ValidationProcessor.validate(fieldContext.getField(), transformedValue, validationHandler.getValidators(), this);
                    }
                } else if (!fieldContext.isTransient()) {
                    if (options.contains(ConfigOptionEnum.REPLACE_DEFAULT_WITH_TRANSFORMED)) {
                        defaultValue = TransformerProcessor.transform(fieldContext.getField(), defaultValue, transformerHandler.getTransformers(), this);

                        if (options.contains(ConfigOptionEnum.CHECK_DEFAULT_FOR_VALIDATION)) { // Revalidate.
                            ValidationProcessor.validate(fieldContext.getField(), defaultValue, validationHandler.getValidators(), this);
                        }
                    }

                    configHandler.set(fieldContext, defaultValue);

                    didChange = true;
                }

                fieldContext.pop();
            } else if (context instanceof CommentContext) {
                CommentContext commentContext = (CommentContext) context;

                if (configHandler.exists(commentContext.getLocation())) {
                    if (configHandler.shouldUpdateComments(commentContext)) {
                        configHandler.setComment(commentContext);
                        didChange = true;
                    }
                } else {
                    configHandler.setComment(commentContext);
                    didChange = true;
                }
            }
        }

        if (didChange)
            configHandler.saveConfig();
    }
}
