package cc.kermanispretty.config.common.transform.processer;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.transform.exepctions.InvalidTransformExpectation;
import cc.kermanispretty.config.common.transform.wrapped.WrappedTransformer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;

public class TransformerProcessor {


    public static Object transform(Field field, Object object, HashSet<WrappedTransformer<?, ?, ?>> transformers, Config config) throws InvalidTransformExpectation {
        for (WrappedTransformer<?, ?, ?> transformer : transformers) {
            if (!field.isAnnotationPresent(transformer.getAnnotationClass())) continue;

            Annotation annotation = field.getAnnotation(transformer.getAnnotationClass());

            object = transformer.transform(field, object, annotation, config);
        }

        //No transformers
        return object;
    }
}
