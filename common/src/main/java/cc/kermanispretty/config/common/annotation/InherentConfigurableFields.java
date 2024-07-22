package cc.kermanispretty.config.common.annotation;

import java.lang.annotation.*;

/**
 * Same as see {@link ConfigurableFields} but is inherited by all subclasses.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface InherentConfigurableFields {
}
