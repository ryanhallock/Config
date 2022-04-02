package cc.kermanispretty.config.common.annotation.validation.impl;

import cc.kermanispretty.config.common.annotation.validation.ValidatorIs;
import cc.kermanispretty.config.common.validation.validator.impl.IntRangeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@ValidatorIs(IntRangeValidator.class)
public @interface IntRange {
    int min() default 0;
    int max() default 1;
}
