package cc.kermanispretty.config.common.validation.annotation.impl;

import cc.kermanispretty.config.common.validation.annotation.Validation;
import cc.kermanispretty.config.common.validation.impl.StringRangeValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Validation(value = StringRangeValidator.class, type = String.class)
public @interface StringRange {
    int min() default 0;

    int max() default 1;
}
