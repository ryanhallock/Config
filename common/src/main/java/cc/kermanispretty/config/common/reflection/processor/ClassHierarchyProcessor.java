package cc.kermanispretty.config.common.reflection.processor;

import cc.kermanispretty.config.common.ConfigOptionEnum;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.LinkedHashSet;

public class ClassHierarchyProcessor {

    //(Interface Class -> Super Class -> Declaring Class -> Class) recursive search optional.
    public static void process(LinkedHashSet<Class<?>> prependingClasses, Class<?> owningClazz, EnumSet<ConfigOptionEnum> configOptions) {
        if (configOptions.contains(ConfigOptionEnum.SEARCH_INTERFACES)) {
            prependingClasses.addAll(Arrays.asList(owningClazz.getInterfaces()));
        }

        if (configOptions.contains(ConfigOptionEnum.SEARCH_SUPER_CLASSES)) {
            for (Class<?> superClass = owningClazz.getSuperclass();
                 superClass != null;
                 superClass = superClass.getSuperclass()
            ) {
                prependingClasses.add(superClass);
            }
        }

        if (configOptions.contains(ConfigOptionEnum.SEARCH_DECLARED_CLASSES)) {
            for (Class<?> declaringClass = owningClazz.getDeclaringClass();
                 declaringClass != null;
                 declaringClass = declaringClass.getDeclaringClass()
            ) {
                prependingClasses.add(declaringClass);
            }
        }

        if (configOptions.contains(ConfigOptionEnum.SEARCH_RECURSIVELY)) { // TODO: Experimental
            LinkedHashSet<Class<?>> newClasses = new LinkedHashSet<>();

            // Omit SEARCH_CURRENT_CLASS from the recursive search.
            EnumSet<ConfigOptionEnum> omittedOptions = EnumSet.copyOf(configOptions);
            omittedOptions.remove(ConfigOptionEnum.SEARCH_CURRENT_CLASS);

            for (Class<?> clazz : prependingClasses) {
                process(newClasses, clazz, omittedOptions);
            }

            newClasses.addAll(prependingClasses);

            prependingClasses.clear(); // Cant reassign to prependingClasses because its not in this scope.
            prependingClasses.addAll(newClasses);
        }

        if (configOptions.contains(ConfigOptionEnum.SEARCH_CURRENT_CLASS)) {
            prependingClasses.add(owningClazz);
        }
    }
}
