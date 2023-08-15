package cc.kermanispretty.config.common.reflection.processor;

import cc.kermanispretty.config.common.ConfigOptionEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

public class ClassHierarchyProcessor {

    //(Interface Class -> Super Class -> Declaring Class -> Class) recursive search optional.
    public static ArrayList<Class<?>> process(Class<?> owningClazz, ArrayList<Class<?>> prependingClasses, EnumSet<ConfigOptionEnum> configOptions) {
        if (prependingClasses == null) {
            prependingClasses = new ArrayList<>();
        }

        if (owningClazz == null) {
            return prependingClasses;
        }

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
            ArrayList<Class<?>> newClasses = new ArrayList<>();

            // Omit SEARCH_CURRENT_CLASS from the recursive search.
            EnumSet<ConfigOptionEnum> optionEnums = EnumSet.copyOf(configOptions);
            optionEnums.remove(ConfigOptionEnum.SEARCH_CURRENT_CLASS);

            for (Class<?> clazz : prependingClasses) {
                newClasses.addAll(process(clazz, prependingClasses, optionEnums));
            }

            prependingClasses = newClasses;
        }

        if (configOptions.contains(ConfigOptionEnum.SEARCH_CURRENT_CLASS)) {
            prependingClasses.add(owningClazz);
        }

        return prependingClasses;
    }
}
