package cc.kermanispretty.config.common.validation.validator.impl;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.annotation.validation.impl.IntRange;
import cc.kermanispretty.config.common.annotation.validation.impl.StringRange;
import cc.kermanispretty.config.common.validation.validator.Validator;
import cc.kermanispretty.config.common.validation.validator.error.InvalidValidationExpectation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class StringRangeValidator implements Validator {
    @Override
    public boolean validate(Field field, Object object, Annotation annotation, Config config) throws InvalidValidationExpectation {
        StringRange range = (StringRange) annotation;

        String value = (String) object; // string is not null
        int length = value.length();

        if (length >= range.min() && range.max() >= length) {
            return true;
        }

        throw new InvalidValidationExpectation(config, field, value + " not in range of " + range.min() + " to " + range.max());
    }
}
