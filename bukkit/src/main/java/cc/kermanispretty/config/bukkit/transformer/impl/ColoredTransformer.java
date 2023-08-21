package cc.kermanispretty.config.bukkit.transformer.impl;

import cc.kermanispretty.config.bukkit.transformer.annotation.Colored;
import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.transform.Transformer;
import cc.kermanispretty.config.common.transform.exepctions.InvalidTransformExpectation;
import org.bukkit.ChatColor;

import java.lang.reflect.Field;
import java.util.List;

public class ColoredTransformer implements Transformer<Object, Colored, Object> {
    @Override
    @SuppressWarnings("unchecked")
    public Object transform(Field field, Object object, Colored annotation, Config config) throws InvalidTransformExpectation {
        if (object == null) return null;

        if (object instanceof List) {
            List<String> list = (List<String>) object;

            list.replaceAll(string -> ChatColor.translateAlternateColorCodes(annotation.value(), string));

            return list; // Not really required. But just in case.
        } else if (object instanceof String) {
            return ChatColor.translateAlternateColorCodes(annotation.value(), (String) object);
        }

        throw new InvalidTransformExpectation("Invalid object type for LegacyColorTransformer, must be String or List<String>");
    }
}
