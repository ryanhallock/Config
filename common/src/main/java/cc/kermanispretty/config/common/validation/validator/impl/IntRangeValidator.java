package cc.kermanispretty.config.common.validation.validator.impl;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.annotation.validation.impl.IntRange;
import cc.kermanispretty.config.common.validation.validator.Validator;
import cc.kermanispretty.config.common.validation.validator.error.InvalidValidationExpectation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class IntRangeValidator implements Validator {
    @Override
    public boolean validate(Field field, Object object, Annotation annotation, Config config) throws InvalidValidationExpectation {
        IntRange range = (IntRange) annotation;

        int value = (int) object;

        if (value >= range.min() && range.max() >= value) {
            return true;
        }

        throw new InvalidValidationExpectation(config, field, value + " not in range of " + range.min() + " to " + range.max());
    }
}
