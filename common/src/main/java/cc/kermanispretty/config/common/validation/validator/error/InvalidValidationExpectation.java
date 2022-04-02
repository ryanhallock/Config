package cc.kermanispretty.config.common.validation.validator.error;

import cc.kermanispretty.config.common.Config;

import java.lang.reflect.Field;

public class InvalidValidationExpectation extends RuntimeException {
    public InvalidValidationExpectation(String message) {
        super(message);
    }

    public InvalidValidationExpectation(Field field, String message) {
        super(field.getName() + message);
    }

    public InvalidValidationExpectation(Config config, Field field, String message) {
        super(new Throwable(config.getPrefix(field.getDeclaringClass()) + field.getName() + ":" + message));
    }
}
