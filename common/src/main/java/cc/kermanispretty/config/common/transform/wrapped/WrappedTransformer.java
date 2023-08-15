package cc.kermanispretty.config.common.transform.wrapped;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.annotation.transfom.Transform;
import cc.kermanispretty.config.common.transform.Transformer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class WrappedTransformer<T, U, S extends Annotation> {

    private final Transformer<T, U, S> transformer;
    private final Class<T> objectClass;
    private final Class<S> annotationClass;


    @SuppressWarnings("unchecked")
    public WrappedTransformer(Class<? extends S> annotation, Transform transform) {
        try {
            this.transformer = (Transformer<T, U, S>) transform.value().newInstance();
            this.objectClass = (Class<T>) transform.type();
            this.annotationClass = (Class<S>) annotation;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public WrappedTransformer(Transformer<T, U, S> transformer, Class<T> objectClass, Class<S> annotation) {
        this.transformer = transformer;
        this.objectClass = objectClass;
        this.annotationClass = annotation;
    }

    @SuppressWarnings("unchecked")
    public U transform(Field field, Object object, Annotation annotation, Config config) {
        if (object != null && !objectClass.isAssignableFrom(object.getClass()))
            throw new RuntimeException(String.format("Validator %s cannot cast %s is not assignable from %s", transformer.getClass(), object.getClass(), objectClass));
        if (!annotationClass.isAssignableFrom(annotation.getClass()))
            throw new RuntimeException(String.format("Validator %s cannot cast %s is not assignable from %s", transformer.getClass(), annotation.getClass(), annotationClass));

        // We dont check U, because we expect the generic to warn if it is not assignable.
        return transformer.transform(
                field,
                (T) object,
                (S) annotation,
                config
        );
    }

    public Transformer<T, U, S> getTransformer() {
        return transformer;
    }

    public Class<T> getObjectClass() {
        return objectClass;
    }

    public Class<S> getAnnotationClass() {
        return annotationClass;
    }
}
