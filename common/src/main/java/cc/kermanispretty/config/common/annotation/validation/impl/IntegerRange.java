package cc.kermanispretty.config.common.annotation.validation.impl;

import cc.kermanispretty.config.common.annotation.validation.Validator;
import cc.kermanispretty.config.common.validation.impl.IntegerRangeValidation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Validator(value = IntegerRangeValidation.class, type = Integer.class)
public @interface IntegerRange {
    int min() default 0;
    int max() default 1;
}
