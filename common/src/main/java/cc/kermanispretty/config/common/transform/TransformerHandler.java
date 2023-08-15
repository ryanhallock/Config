package cc.kermanispretty.config.common.transform;

import cc.kermanispretty.config.common.annotation.transfom.Transform;
import cc.kermanispretty.config.common.transform.wrapped.WrappedTransformer;

import java.lang.annotation.Annotation;
import java.util.HashSet;

public class TransformerHandler {
    private final HashSet<WrappedTransformer<?, ?, ?>> transformers;

    public TransformerHandler() {
        this.transformers = new HashSet<>();
    }

    @SafeVarargs
    public TransformerHandler(Class<? extends Annotation>... annotations) {
        this();
        for (Class<? extends Annotation> annotation : annotations) {
            register(annotation);
        }
    }


    public void register(TransformerHandler handler) {
        this.transformers.addAll(handler.transformers);
    }

    public void register(Class<? extends Annotation> annotation) {
        if (!annotation.isAnnotationPresent(Transform.class))
            throw new IllegalArgumentException("Transform annotations must be annotated with @Transform");

        Transform transform = annotation.getAnnotation(Transform.class);

        transformers.add(new WrappedTransformer<>(annotation, transform));
    }

    public HashSet<WrappedTransformer<?, ?, ?>> getTransformers() {
        return transformers;
    }
}
