package cc.kermanispretty.config.common.transform.exepctions;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.reflection.context.FieldContext;
import cc.kermanispretty.config.common.reflection.processor.AnnotationLocationProcessor;

import java.lang.reflect.Field;

public class InvalidTransformExpectation extends RuntimeException {
    public InvalidTransformExpectation(String message) {
        super(message);
    }

    public InvalidTransformExpectation(Field field, String message) {
        super(field.getName() + message);
    }

    public InvalidTransformExpectation(Config config, Field field, String message) {
        super(new Throwable(String.format("Config: %s, Field: %s, Message: %s", config.getClass().getName(), AnnotationLocationProcessor.processSimpleName(field), message)));
    }

    public InvalidTransformExpectation(Config config, FieldContext fieldContext, String message) {
        this(config, fieldContext.getField(), message);
    }
}
