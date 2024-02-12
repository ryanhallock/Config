package cc.kermanispretty.config.common.validation.wrapped;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.reflection.context.FieldContext;
import cc.kermanispretty.config.common.validation.Validator;
import cc.kermanispretty.config.common.validation.annotation.Validation;

import java.lang.annotation.Annotation;

public class WrappedValidator<T, S extends Annotation> {

    private final Validator<T, S> validator;
    private final Class<T> objectClass;
    private final Class<S> annotationClass;


    @SuppressWarnings("unchecked") // Will runtime error if anything is wrong so...
    public WrappedValidator(Class<? extends S> annotation, Validation validation) {
        try { // a whole lot of unchecked casting...
            this.validator = (Validator<T, S>) validation.value().newInstance();
            this.objectClass = (Class<T>) validation.type();
            this.annotationClass = (Class<S>) annotation;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public WrappedValidator(Validator<T, S> validator, Class<T> objectClass, Class<S> annotation) {
        this.validator = validator;
        this.objectClass = objectClass;
        this.annotationClass = annotation;
    }

    @SuppressWarnings("unchecked") // We already check if they can be casted
    public boolean validate(FieldContext fieldContext, Object object, Annotation annotation, Config config) {
        if (object != null && !objectClass.isAssignableFrom(object.getClass()))
            throw new RuntimeException(String.format("Validator %s cannot cast %s is not assignable from %s", validator.getClass(), object.getClass(), objectClass));
        if (!annotationClass.isAssignableFrom(annotation.getClass()))
            throw new RuntimeException(String.format("Validator %s cannot cast %s is not assignable from %s", validator.getClass(), annotation.getClass(), annotationClass));

        return validator.validate(
                fieldContext,
                (T) object,
                (S) annotation,
                config
        );
    }

    public Validator<T, S> getValidator() {
        return validator;
    }

    public Class<T> getObjectClass() {
        return objectClass;
    }

    public Class<S> getAnnotationClass() {
        return annotationClass;
    }
}
