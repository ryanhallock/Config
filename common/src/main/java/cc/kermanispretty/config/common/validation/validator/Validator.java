package cc.kermanispretty.config.common.validation.validator;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.validation.validator.error.InvalidValidationExpectation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * A default constructor is REQUIRED.
 */
public interface Validator {
    /**
     * Validates the given object. If the object is not valid, an exception is thrown.
     *
     * @param field      The field to validate.
     * @param object     The object to validate.
     * @param annotation The annotation associated with the field.
     * @param config The config associated with the field.
     * @return true if its valid
     */
    boolean validate(Field field, Object object, Annotation annotation, Config config) throws InvalidValidationExpectation;
}
