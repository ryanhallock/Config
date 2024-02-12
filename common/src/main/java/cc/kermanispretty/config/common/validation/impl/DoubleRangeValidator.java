package cc.kermanispretty.config.common.validation.impl;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.reflection.context.FieldContext;
import cc.kermanispretty.config.common.validation.Validator;
import cc.kermanispretty.config.common.validation.annotation.impl.DoubleRange;
import cc.kermanispretty.config.common.validation.exepctions.InvalidValidationExpectation;

public class DoubleRangeValidator implements Validator<Double, DoubleRange> {
    @Override
    public boolean validate(FieldContext field, Double value, DoubleRange range, Config config) throws InvalidValidationExpectation {
        if (value >= range.min() && value <= range.max()) {
            return true;
        }

        throw new InvalidValidationExpectation(config, field, value + " not in range of " + range.min() + " to " + range.max());
    }
}
