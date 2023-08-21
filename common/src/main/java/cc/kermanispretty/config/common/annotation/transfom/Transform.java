package cc.kermanispretty.config.common.annotation.transfom;

import cc.kermanispretty.config.common.transform.Transformer;

import java.lang.annotation.*;


@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transform {
    /**
     * The validator class that will be used to validate the annotated field. @See Validator
     */
    Class<? extends Transformer<?, ? extends Annotation, ?>> value();

    /**
     * Kind of hate this but, generics. This is the type that is cast by the object so like Double.class or Integer.class
     */
    Class<?> type() default Object.class;
}
