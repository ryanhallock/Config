package cc.kermanispretty.config.common.validation.exepctions;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.reflection.processor.AnnotationLocationProcessor;

import java.lang.reflect.Field;

public class InvalidValidationExpectation extends RuntimeException {
    public InvalidValidationExpectation(String message) {
        super(message);
    }

    public InvalidValidationExpectation(Field field, String message) {
        super(field.getName() + message);
    }

    public InvalidValidationExpectation(Config config, Field field, String message) {
        super(new Throwable(String.format("Config: %s, Field: %s, Message: %s", config.getClass().getName(), AnnotationLocationProcessor.processSimpleName(field), message)));
    }
}
