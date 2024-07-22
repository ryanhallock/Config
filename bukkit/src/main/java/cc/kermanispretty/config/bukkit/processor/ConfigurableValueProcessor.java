package cc.kermanispretty.config.bukkit.processor;

import cc.kermanispretty.config.common.reflection.context.FieldContext;
import cc.kermanispretty.config.common.reflection.processor.AnnotationLocationProcessor;
import cc.kermanispretty.config.common.reflection.processor.ConfigurableFieldProcessor;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigurableValueProcessor {

    //TODO rewrite, as this shouldnt require redecleration of contexts.

    // We use this to make our own steralizer.
    public static Object process(Object object) throws IllegalAccessException {
        if (object == null) {
            return null;
        }

        Set<Field> configurableFields = ConfigurableFieldProcessor.process(object.getClass());

        if (configurableFields.isEmpty()) {
            return object; // By default we dont know what to do with it, just return the object.
        }
        Map<String, Object> processedFields = new HashMap<>();

        for (Field field : configurableFields) {
            FieldContext fieldContext = new FieldContext(field, AnnotationLocationProcessor.processSimpleName(field), object);

            fieldContext.push();

            processedFields.put(fieldContext.getLocation(), process(fieldContext.getValue()));

            fieldContext.pop();
        }

        return processedFields;
    }
}
