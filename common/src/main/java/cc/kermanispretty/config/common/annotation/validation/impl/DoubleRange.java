package cc.kermanispretty.config.common.annotation.validation.impl;

import cc.kermanispretty.config.common.annotation.validation.ValidatorIs;
import cc.kermanispretty.config.common.validation.validator.impl.DoubleRangeValidator;
import cc.kermanispretty.config.common.validation.validator.impl.IntRangeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ValidatorIs(DoubleRangeValidator.class)
public @interface DoubleRange {
    double min() default 0;
    double max() default 1;
}
