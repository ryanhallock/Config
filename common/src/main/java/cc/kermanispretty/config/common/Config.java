package cc.kermanispretty.config.common;

import cc.kermanispretty.config.common.annotation.Configurable;
import cc.kermanispretty.config.common.location.ConfigurablePrefix;
import cc.kermanispretty.config.common.location.context.LocationContext;
import cc.kermanispretty.config.common.reflection.context.CommentContext;
import cc.kermanispretty.config.common.reflection.context.FieldContext;
import cc.kermanispretty.config.common.reflection.processor.AnnotationLocationProcessor;
import cc.kermanispretty.config.common.reflection.processor.ClassHierarchyProcessor;
import cc.kermanispretty.config.common.reflection.processor.CommentContextProcessor;
import cc.kermanispretty.config.common.transform.TransformerHandler;
import cc.kermanispretty.config.common.transform.processer.TransformerProcessor;
import cc.kermanispretty.config.common.validation.ValidatorHandler;
import cc.kermanispretty.config.common.validation.processer.ValidationProcessor;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class Config {
    private final ConfigHandler configHandler;
    private final ValidatorHandler validatorHandler;
    private final TransformerHandler transformerHandler;
    private final EnumSet<ConfigOptionEnum> options;
    private final LinkedHashSet<LocationContext> locationContexts;

    public Config(ConfigHandler configHandler, ValidatorHandler validatorHandler, TransformerHandler transformerHandler, EnumSet<ConfigOptionEnum> options) {
        this.configHandler = configHandler;
        this.validatorHandler = validatorHandler;
        this.transformerHandler = transformerHandler;
        this.options = options;
        this.locationContexts = new LinkedHashSet<>();
    }

    public void unregister(Object object) {
        locationContexts.removeIf(fieldContext -> // TODO: Dirty one line and could be buggy with nulls
                fieldContext.getOwningClass().equals(object.getClass()));
    }

    public void register(Object... objects) throws IllegalAccessException {
        for (Object object : objects) {
            boolean isClass = object instanceof Class<?>;
            Class<?> owningClass = isClass ? (Class<?>) object : object.getClass();
            Object instance = isClass ? null : object;

            String separator = configHandler.separator(); // Store an instance of the separator.
            LinkedHashSet<Class<?>> classHierarchy = new LinkedHashSet<>();

            ClassHierarchyProcessor.process(classHierarchy, owningClass, options);

            Stream<Class<?>> classStream = classHierarchy.stream()
                    .filter(clazz -> clazz.isAnnotationPresent(Configurable.class));

            LinkedHashSet<Class<?>> configurableClassHierarchy = new LinkedHashSet<>();

            classStream.forEachOrdered(clazz -> {
                configurableClassHierarchy.add(clazz); // Append it to the copy.

                String location = AnnotationLocationProcessor.process(configurableClassHierarchy, separator);

                //Duplicates can happen, that is another reason we use a HashSet.
                CommentContextProcessor.process(locationContexts, clazz, location, instance, false);
            });

            //Use the previous hierarchy to process fields.
            StringBuilder prefix = new StringBuilder(AnnotationLocationProcessor.process(configurableClassHierarchy, separator));

            if (!isClass && instance instanceof ConfigurablePrefix) // Dynamic prefix support.
                prefix.append(separator)
                        .append(((ConfigurablePrefix) instance).getConfigurablePrefix());

            LinkedHashSet<Field> configurableFields = new LinkedHashSet<>();

            Deque<Class<?>> classHierarchyDeque = new ArrayDeque<>(classHierarchy); // We use a deque to reverse the order.
            classHierarchyDeque.descendingIterator().forEachRemaining(clazz -> configurableFields.addAll(
                    Arrays.stream(clazz.getDeclaredFields())
                            .filter(field -> field.isAnnotationPresent(Configurable.class))
                            .collect(Collectors.toSet()))
            );

            configurableFields.forEach(field -> {
                String location = prefix + separator + AnnotationLocationProcessor.processSimpleName(field);

                FieldContext context = new FieldContext(field, location, instance) {
                };

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
                        ValidationProcessor.validate(fieldContext, defaultValue, validatorHandler.getValidators(), this);
                    }

                    Object transformedValue = TransformerProcessor.transform(fieldContext, defaultValue, transformerHandler.getTransformers(), this);

                    if (defaultValue != transformedValue)
                        fieldContext.set(transformedValue);

                    if (options.contains(ConfigOptionEnum.CHECK_DEFAULT_TRANSFORMED_FOR_VALIDATION) && defaultValue != transformedValue) {
                        ValidationProcessor.validate(fieldContext, transformedValue, validatorHandler.getValidators(), this);
                    }
                }

                if (configHandler.exists(fieldContext.getLocation())) {
                    Object value = configHandler.get(fieldContext);

                    if (options.contains(ConfigOptionEnum.CHECK_CONFIG_FOR_VALIDATION)) {
                        ValidationProcessor.validate(fieldContext, value, validatorHandler.getValidators(), this);
                    }

                    Object transformedValue = TransformerProcessor.transform(fieldContext, value, transformerHandler.getTransformers(), this);

                    if (defaultValue != transformedValue)
                        fieldContext.set(transformedValue);

                    if (options.contains(ConfigOptionEnum.CHECK_CONFIG_TRANSFORMED_FOR_VALIDATION) && value != transformedValue) {
                        ValidationProcessor.validate(fieldContext, transformedValue, validatorHandler.getValidators(), this);
                    }
                } else if (!fieldContext.isTransient()) {
                    if (options.contains(ConfigOptionEnum.REPLACE_DEFAULT_WITH_TRANSFORMED)) {
                        defaultValue = TransformerProcessor.transform(fieldContext, defaultValue, transformerHandler.getTransformers(), this);

                        if (options.contains(ConfigOptionEnum.CHECK_DEFAULT_FOR_VALIDATION)) { // Revalidate.
                            ValidationProcessor.validate(fieldContext, defaultValue, validatorHandler.getValidators(), this);
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
