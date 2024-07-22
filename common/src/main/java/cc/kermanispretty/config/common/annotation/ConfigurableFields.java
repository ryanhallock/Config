package cc.kermanispretty.config.common.annotation;

import java.lang.annotation.*;


/***
 * Allows all fields to be considered Configurable in the class.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurableFields {
}
