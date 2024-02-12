package cc.kermanispretty.config.common.annotation;

import java.lang.annotation.*;

/**
 * Inherent fields if they arent present.
 * <p>
 * For custom annotations you should just use @Inherited if it just targets TYPE.
 */
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface InherentFields {
}
