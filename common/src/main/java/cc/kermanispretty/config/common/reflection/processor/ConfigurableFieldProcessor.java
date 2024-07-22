package cc.kermanispretty.config.common.reflection.processor;

import cc.kermanispretty.config.common.annotation.Configurable;
import cc.kermanispretty.config.common.annotation.ConfigurableFields;
import cc.kermanispretty.config.common.annotation.InherentConfigurableFields;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigurableFieldProcessor {

    public static Set<Field> process(Class<?> clazz) {
        boolean includeFields = clazz.isAnnotationPresent(ConfigurableFields.class) || clazz.isAnnotationPresent(InherentConfigurableFields.class);
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Configurable.class)
                        || (includeFields && !Modifier.isTransient(field.getModifiers()))) // Dont allow transient fields to be serialized
                .collect(Collectors.toSet());
    }
}
