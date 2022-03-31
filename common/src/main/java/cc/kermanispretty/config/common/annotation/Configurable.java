package cc.kermanispretty.config.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Configurable {
    /**
     * If used on a class it will force all fields to have the same prefix.
     * @return location of the configuration file location
     */

    String value();
}
