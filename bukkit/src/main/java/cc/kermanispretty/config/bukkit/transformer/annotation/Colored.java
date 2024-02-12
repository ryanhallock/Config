package cc.kermanispretty.config.bukkit.transformer.annotation;

import cc.kermanispretty.config.bukkit.transformer.impl.ColoredTransformer;
import cc.kermanispretty.config.common.transform.annotation.Transform;

// Currently only supports String and List<String>
@Transform(ColoredTransformer.class)
public @interface Colored {
    char value() default '&';
}
