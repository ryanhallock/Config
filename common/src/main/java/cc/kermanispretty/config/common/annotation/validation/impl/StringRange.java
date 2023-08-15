package cc.kermanispretty.config.common.annotation.validation.impl;

import cc.kermanispretty.config.common.annotation.validation.Validator;
import cc.kermanispretty.config.common.validation.impl.StringRangeValidation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Validator(value = StringRangeValidation.class, type = String.class)
public @interface StringRange {
    int min() default 0;
    int max() default 1;
}
