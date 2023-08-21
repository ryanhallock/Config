package cc.kermanispretty.config.common.validation.impl;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.annotation.validation.impl.FloatRange;
import cc.kermanispretty.config.common.validation.Validator;
import cc.kermanispretty.config.common.validation.exepctions.InvalidValidationExpectation;

import java.lang.reflect.Field;

public class FloatRangeValidator implements Validator<Float, FloatRange> {
    @Override
    public boolean validate(Field field, Float value, FloatRange range, Config config) throws InvalidValidationExpectation {
        if (value >= range.min() && value <= range.max()) {
            return true;
        }

        throw new InvalidValidationExpectation(config, field, value + " not in range of " + range.min() + " to " + range.max());
    }
}
