package cc.kermanispretty.config.common.validation.impl;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.annotation.validation.impl.StringRange;
import cc.kermanispretty.config.common.validation.Validator;
import cc.kermanispretty.config.common.validation.exepctions.InvalidValidationExpectation;

import java.lang.reflect.Field;

public class StringRangeValidator implements Validator<String, StringRange> {
    @Override
    public boolean validate(Field field, String value, StringRange range, Config config) throws InvalidValidationExpectation {
        int length = value.length();

        if (length >= range.min() && length <= range.max()) {
            return true;
        }

        throw new InvalidValidationExpectation(config, field, value + " not in range of " + range.min() + " to " + range.max());
    }
}
