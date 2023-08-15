package cc.kermanispretty.config.common.reflection.processor;

import cc.kermanispretty.config.common.annotation.Configurable;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class AnnotationLocationProcessor {

    public static String process(ArrayList<Class<?>> elements, String separator) {
        return elements
                .stream()
                .map(AnnotationLocationProcessor::processSimpleName)
                .collect(Collectors.joining(separator));
    }

    //use field or class name if empty
    public static String processSimpleName(AnnotatedElement annotatedElement) {
        String location = annotatedElement.getAnnotation(Configurable.class).value();
        if (location.isEmpty() || location.equals("null")) {
            if (annotatedElement instanceof Field) {
                location = ((Field) annotatedElement).getName();
            } else {
                location = ((Class<?>) annotatedElement).getSimpleName();
            }
        }
        return location;
    }
}
