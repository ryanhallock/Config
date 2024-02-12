package cc.kermanispretty.config.common.validation.annotation.impl;

import cc.kermanispretty.config.common.validation.annotation.Validation;
import cc.kermanispretty.config.common.validation.impl.FloatRangeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Validation(value = FloatRangeValidator.class, type = Float.class)
public @interface FloatRange {
    float min() default 0;

    float max() default 1;
}
