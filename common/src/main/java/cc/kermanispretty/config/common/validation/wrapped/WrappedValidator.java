package cc.kermanispretty.config.common.validation.wrapped;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.annotation.validation.Validator;
import cc.kermanispretty.config.common.validation.Validation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class WrappedValidator<T, S extends Annotation> {

    private final Validation<T, S> validator;
    private final Class<T> objectClass;
    private final Class<S> annotationClass;


    @SuppressWarnings("unchecked") // Will runtime error if anything is wrong so...
    public WrappedValidator(Class<? extends S> annotation, Validator validator) {
        try { // a whole lot of unchecked casting...
            this.validator = (Validation<T, S>) validator.value().newInstance();
            this.objectClass = (Class<T>) validator.type();
            this.annotationClass = (Class<S>) annotation;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public WrappedValidator(Validation<T, S> validator, Class<T> objectClass, Class<S> annotation) {
        this.validator = validator;
        this.objectClass = objectClass;
        this.annotationClass = annotation;
    }

    @SuppressWarnings("unchecked") // We already check if they can be casted
    public boolean validate(Field field, Object object, Annotation annotation, Config config) {
        if (object != null && !objectClass.isAssignableFrom(object.getClass()))
            throw new RuntimeException(String.format("Validator %s cannot cast %s is not assignable from %s", validator.getClass(), object.getClass(), objectClass));
        if (!annotationClass.isAssignableFrom(annotation.getClass()))
            throw new RuntimeException(String.format("Validator %s cannot cast %s is not assignable from %s", validator.getClass(), annotation.getClass(), annotationClass));

        return validator.validate(
                field,
                (T) object,
                (S) annotation,
                config
        );
    }

    public Validation<T, S> getValidator() {
        return validator;
    }

    public Class<T> getObjectClass() {
        return objectClass;
    }

    public Class<S> getAnnotationClass() {
        return annotationClass;
    }
}
