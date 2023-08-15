package cc.kermanispretty.config.common.annotation.validation.impl;

import cc.kermanispretty.config.common.annotation.validation.Validator;
import cc.kermanispretty.config.common.validation.impl.DoubleRangeValidation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Validator(value = DoubleRangeValidation.class, type = Double.class)
public @interface DoubleRange {
    double min() default 0;
    double max() default 1;
}
