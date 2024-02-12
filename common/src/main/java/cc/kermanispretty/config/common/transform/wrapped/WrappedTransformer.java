package cc.kermanispretty.config.common.transform.wrapped;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.reflection.context.FieldContext;
import cc.kermanispretty.config.common.transform.Transformer;
import cc.kermanispretty.config.common.transform.annotation.Transform;

import java.lang.annotation.Annotation;

public class WrappedTransformer<T, V extends Annotation, R> {

    private final Transformer<T, V, R> transformer;
    private final Class<T> objectClass;
    private final Class<V> annotationClass;


    @SuppressWarnings("unchecked")
    public WrappedTransformer(Class<? extends V> annotation, Transform transform) {
        try {
            this.transformer = (Transformer<T, V, R>) transform.value().newInstance();
            this.objectClass = (Class<T>) transform.type();
            this.annotationClass = (Class<V>) annotation;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public WrappedTransformer(Transformer<T, V, R> transformer, Class<T> objectClass, Class<V> annotation) {
        this.transformer = transformer;
        this.objectClass = objectClass;
        this.annotationClass = annotation;
    }

    @SuppressWarnings("unchecked")
    public R transform(FieldContext fieldContext, Object object, Annotation annotation, Config config) {
        if (object != null && !objectClass.isAssignableFrom(object.getClass()))
            throw new RuntimeException(String.format("Validator %s cannot cast %s is not assignable from %s", transformer.getClass(), object.getClass(), objectClass));
        if (!annotationClass.isAssignableFrom(annotation.getClass()))
            throw new RuntimeException(String.format("Validator %s cannot cast %s is not assignable from %s", transformer.getClass(), annotation.getClass(), annotationClass));

        // We dont check U, because we expect the generic to warn if it is not assignable.
        return transformer.transform(
                fieldContext,
                (T) object,
                (V) annotation,
                config
        );
    }

    public Transformer<T, V, R> getTransformer() {
        return transformer;
    }

    public Class<T> getObjectClass() {
        return objectClass;
    }

    public Class<V> getAnnotationClass() {
        return annotationClass;
    }
}
