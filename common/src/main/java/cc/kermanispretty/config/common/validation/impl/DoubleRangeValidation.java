package cc.kermanispretty.config.common.validation.impl;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.annotation.validation.impl.DoubleRange;
import cc.kermanispretty.config.common.validation.Validation;
import cc.kermanispretty.config.common.validation.exepctions.InvalidValidationExpectation;

import java.lang.reflect.Field;

public class DoubleRangeValidation implements Validation<Double, DoubleRange> {
    @Override
    public boolean validate(Field field, Double value, DoubleRange range, Config config) throws InvalidValidationExpectation {
        if (value >= range.min() && value <= range.max()) {
            return true;
        }

        throw new InvalidValidationExpectation(config, field, value + " not in range of " + range.min() + " to " + range.max());
    }
}
