package cc.kermanispretty.config.common.annotation.validation.impl;

import cc.kermanispretty.config.common.annotation.validation.ValidatorIs;
import cc.kermanispretty.config.common.validation.validator.impl.FloatRangeValidator;
import cc.kermanispretty.config.common.validation.validator.impl.IntRangeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ValidatorIs(FloatRangeValidator.class)
public @interface FloatRange {
    float min() default 0;
    float max() default 1;
}
