package cc.kermanispretty.config.common.annotation.validation.impl;

import cc.kermanispretty.config.common.annotation.validation.Validator;
import cc.kermanispretty.config.common.validation.impl.FloatRangeValidation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Validator(value = FloatRangeValidation.class, type = Float.class)
public @interface FloatRange {
    float min() default 0;
    float max() default 1;
}
