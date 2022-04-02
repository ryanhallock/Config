package cc.kermanispretty.config.common.validation;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.ConfigOptions;
import cc.kermanispretty.config.common.annotation.validation.ValidatorIs;
import cc.kermanispretty.config.common.annotation.validation.impl.FloatRange;
import cc.kermanispretty.config.common.annotation.validation.impl.IntRange;
import cc.kermanispretty.config.common.annotation.validation.impl.StringRange;
import cc.kermanispretty.config.common.validation.validator.Validator;
import cc.kermanispretty.config.common.validation.validator.error.InvalidValidationExpectation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;

public class ValidationHandler {
    public final static ValidationHandler DEFAULT = createDefault();
    private HashMap<Class<? extends Annotation>, Validator> lookupMap;
    private ConfigOptions options = ConfigOptions.DEFAULT;
    public ValidationHandler() {
        lookupMap = new HashMap<>();
    }

    public ValidationHandler(ConfigOptions options) {
        this();

        this.options = options;
    }

    public void unload() {
        lookupMap.clear();
        lookupMap = null;
        options = null;
    }

    private static ValidationHandler createDefault() {
        ValidationHandler handler = new ValidationHandler();

        Arrays.asList(
                IntRange.class,
                FloatRange.class,
                IntRange.class,
                StringRange.class
        ).forEach(handler::register);

        return handler;
    }

    public void register(ValidationHandler handler) {
        lookupMap.putAll(handler.lookupMap);
    }

    public void register(Class<? extends Annotation> annotation) {
        if (!annotation.isAnnotationPresent(ValidatorIs.class))
            throw new IllegalArgumentException("Annotation must be annotated with ValidatorIs");

        ValidatorIs validatorIs = annotation.getAnnotation(ValidatorIs.class);

        try {
            lookupMap.put(annotation, validatorIs.value().newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace(); // Class does not have a default constructor with no parameters
        }
    }

    //returns true if the field is valid (unused for api use)
    public boolean checkValidation(Field field, Object object, Config config) {
        for (Annotation annotation : field.getAnnotations()) {
            if (lookupMap.containsKey(annotation.annotationType())) {
                Validator validator = lookupMap.get(annotation.annotationType());

                try {
                    if (!validator.validate(field, object, annotation, config))
                        return false;
                } catch (InvalidValidationExpectation e) {
                    if (!options.suppressValidationErrors)
                        e.printStackTrace();

                    return false;
                }
            }
        }

        return true;
    }
}
