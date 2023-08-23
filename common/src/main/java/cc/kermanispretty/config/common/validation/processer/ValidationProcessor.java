package cc.kermanispretty.config.common.validation.processer;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.reflection.context.FieldContext;
import cc.kermanispretty.config.common.validation.exepctions.InvalidValidationExpectation;
import cc.kermanispretty.config.common.validation.wrapped.WrappedValidator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;

public class ValidationProcessor {


    // Go through each validator and ensure its the correct type and cast the object to the correct type
    public static void validate(FieldContext fieldContext, Object object, HashSet<WrappedValidator<?, ?>> validators, Config config) throws InvalidValidationExpectation {
        Field field = fieldContext.getField();

        for (WrappedValidator<?, ?> validator : validators) {
            if (!field.isAnnotationPresent(validator.getAnnotationClass())) continue;

            Annotation annotation = field.getAnnotation(validator.getAnnotationClass());

            if (!validator.validate(fieldContext, object, annotation, config)) {
                throw new InvalidValidationExpectation(config, field, "Failed to validate field, fallback error");
            }
        }
    }
}
