package cc.kermanispretty.config.common.validation;

import cc.kermanispretty.config.common.validation.annotation.Validation;
import cc.kermanispretty.config.common.validation.annotation.impl.FloatRange;
import cc.kermanispretty.config.common.validation.annotation.impl.IntegerRange;
import cc.kermanispretty.config.common.validation.annotation.impl.StringRange;
import cc.kermanispretty.config.common.validation.wrapped.WrappedValidator;

import java.lang.annotation.Annotation;
import java.util.HashSet;

public class ValidatorHandler {
    @SuppressWarnings("unchecked") // should be safe
    public static final Class<? extends Annotation>[] DEFAULT_IMPL = new Class[]{
            IntegerRange.class,
            FloatRange.class,
            IntegerRange.class,
            StringRange.class
    };

    private final HashSet<WrappedValidator<?, ?>> validators;

    public ValidatorHandler() {
        this.validators = new HashSet<>();
    }

    @SafeVarargs
    public ValidatorHandler(Class<? extends Annotation>... annotations) {
        this();
        for (Class<? extends Annotation> annotation : annotations) {
            register(annotation);
        }
    }


    public void register(ValidatorHandler handler) {
        this.validators.addAll(handler.validators);
    }

    public void register(Class<? extends Annotation> annotation) {
        if (!annotation.isAnnotationPresent(Validation.class))
            throw new IllegalArgumentException("Validator annotations must be annotated with @Validator");

        Validation validation = annotation.getAnnotation(Validation.class);

        validators.add(new WrappedValidator<>(annotation, validation));
    }

    public HashSet<WrappedValidator<?, ?>> getValidators() {
        return validators;
    }
}
