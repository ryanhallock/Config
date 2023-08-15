package cc.kermanispretty.config.common.validation;

import cc.kermanispretty.config.common.annotation.validation.Validator;
import cc.kermanispretty.config.common.annotation.validation.impl.FloatRange;
import cc.kermanispretty.config.common.annotation.validation.impl.IntegerRange;
import cc.kermanispretty.config.common.annotation.validation.impl.StringRange;
import cc.kermanispretty.config.common.validation.wrapped.WrappedValidator;

import java.lang.annotation.Annotation;
import java.util.HashSet;

public class ValidationHandler {
    @SuppressWarnings("unchecked") // should be safe
    public static final Class<? extends Annotation>[] DEFAULT_IMPL = new Class[]{
            IntegerRange.class,
            FloatRange.class,
            IntegerRange.class,
            StringRange.class
    };

    private final HashSet<WrappedValidator<?, ?>> validators;

    public ValidationHandler() {
        this.validators = new HashSet<>();
    }

    @SafeVarargs
    public ValidationHandler(Class<? extends Annotation>... annotations) {
        this();
        for (Class<? extends Annotation> annotation : annotations) {
            register(annotation);
        }
    }


    public void register(ValidationHandler handler) {
        this.validators.addAll(handler.validators);
    }

    public void register(Class<? extends Annotation> annotation) {
        if (!annotation.isAnnotationPresent(Validator.class))
            throw new IllegalArgumentException("Validator annotations must be annotated with @Validator");

        Validator validator = annotation.getAnnotation(Validator.class);

        validators.add(new WrappedValidator<>(annotation, validator));
    }

    public HashSet<WrappedValidator<?, ?>> getValidators() {
        return validators;
    }
}
