package cc.kermanispretty.config.common.reflection.processor;

import cc.kermanispretty.config.common.annotation.Configurable;
import cc.kermanispretty.config.common.annotation.ConfigurableRoot;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class AnnotationLocationProcessor {

    public static String process(LinkedHashSet<Class<?>> elements, String separator) {
        LinkedList<String> locationStack = new LinkedList<>();

        elements.forEach(element -> {
            if (element.isAnnotationPresent(ConfigurableRoot.class)) {
                locationStack.clear();
            }

            locationStack.add(processSimpleName(element));
        });

        return String.join(separator, locationStack);
    }

    public static String processSimpleName(AnnotatedElement annotatedElement) {
        String location = "";

        if (annotatedElement.isAnnotationPresent(Configurable.class)) {
            location = annotatedElement.getAnnotation(Configurable.class).value();
        }

        if (location.isEmpty() || "null".equals(location)) { // Fallback to Field name or Class name.
            if (annotatedElement instanceof Field) {
                location = ((Field) annotatedElement).getName();
            } else {
                location = ((Class<?>) annotatedElement).getSimpleName();
            }
        }

        return location;
    }
}
