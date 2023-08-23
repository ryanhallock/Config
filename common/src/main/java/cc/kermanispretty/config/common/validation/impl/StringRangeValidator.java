package cc.kermanispretty.config.common.validation.impl;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.annotation.validation.impl.StringRange;
import cc.kermanispretty.config.common.reflection.context.FieldContext;
import cc.kermanispretty.config.common.validation.Validator;
import cc.kermanispretty.config.common.validation.exepctions.InvalidValidationExpectation;

public class StringRangeValidator implements Validator<String, StringRange> {
    @Override
    public boolean validate(FieldContext field, String value, StringRange range, Config config) throws InvalidValidationExpectation {
        int length = value.length();

        if (length >= range.min() && length <= range.max()) {
            return true;
        }

        throw new InvalidValidationExpectation(config, field, value + " not in range of " + range.min() + " to " + range.max());
    }
}
