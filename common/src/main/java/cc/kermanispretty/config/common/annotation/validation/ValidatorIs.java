package cc.kermanispretty.config.common.annotation.validation;

import cc.kermanispretty.config.common.validation.validator.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidatorIs {
    Class<? extends Validator> value();
}
