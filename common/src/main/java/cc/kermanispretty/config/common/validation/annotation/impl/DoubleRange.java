package cc.kermanispretty.config.common.validation.annotation.impl;

import cc.kermanispretty.config.common.validation.annotation.Validation;
import cc.kermanispretty.config.common.validation.impl.DoubleRangeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Validation(value = DoubleRangeValidator.class, type = Double.class)
public @interface DoubleRange {
    double min() default 0;

    double max() default 1;
}
