package cc.kermanispretty.config.common;

import cc.kermanispretty.config.common.annotation.Configurable;
import cc.kermanispretty.config.common.annotation.comment.Comment;
import cc.kermanispretty.config.common.annotation.comment.Footer;
import cc.kermanispretty.config.common.annotation.comment.Header;
import cc.kermanispretty.config.common.annotation.comment.InlineComment;
import cc.kermanispretty.config.common.utils.ConfigUtils;
import cc.kermanispretty.config.common.validation.ValidationHandler;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Config {
    private ConfigHandler configHandler;
    private ValidationHandler validationHandler;
    private ConfigOptions options;
    private Set<Object> registeredObjects;

    public Config(ConfigHandler configHandler, ValidationHandler validationHandler, ConfigOptions options) {
        this.configHandler = configHandler;
        this.validationHandler = validationHandler;
        this.options = options;
        this.registeredObjects = new HashSet<>();
    }

    public void register(Object... objects) {
        registeredObjects.addAll(Arrays.asList(objects));
    }

    public void unregister(Object object) {
        registeredObjects.remove(object);
    }

    // Allow for the JVM to clear us faster.
    public void unload() {
        validationHandler.unload();
        registeredObjects.clear();
        validationHandler = null;
        options = null;
        registeredObjects = null;
        configHandler = null;
    }

    public void load() {
        try {
            reload();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    //TODO make this more efficient and make it more readable. (split into smaller functions)
    public void reload() throws IllegalAccessException {
        configHandler.loadConfig();

        boolean didChange = false; //does a batch job if many are changed.

        for (Object object : registeredObjects) {
            boolean isClass = object instanceof Class;
            Class<?> owningClass = isClass ? (Class<?>) object : object.getClass();
            Object instance = isClass ? null : object;
            String prefix = getPrefix(owningClass) + configHandler.separator(); // adds the seperator to the end


            List<String> defaultHeader = ConfigUtils.getHeader(owningClass);
            List<String> defaultFooter = ConfigUtils.getFooter(owningClass);

            List<String> configHeader = configHandler.getHeader();
            List<String> configFooter = configHandler.getFooter();

            if (defaultHeader != null && !defaultHeader.equals(configHeader)) {
                configHandler.setHeader(defaultHeader);
                didChange = true;
            }

            if (defaultFooter != null && !defaultFooter.equals(configFooter)) {
                configHandler.setFooter(defaultFooter);
                didChange = true;
            }

            //Recursive check for all comments sections.
            didChange |= checkSectionComments(owningClass);

            for (Field field : getFields(owningClass)) {
                if (field.isAnnotationPresent(Configurable.class)) {
                    boolean accessible = field.isAccessible();

                    if (!accessible) field.setAccessible(true);

                    //grab the default value from the field
                    Object defaultValue = field.get(instance);

                    if (options.checkDefaultForValidation && defaultValue != null) {
                        validationHandler.checkValidation(field, defaultValue, this);
                    }

                    //grab annotation values
                    String location = prefix + getLocation(field);

                    List<String> defaultComments = ConfigUtils.getComments(field);
                    List<String> defaultInlineComments = ConfigUtils.getInlineComments(field);


                    //grab the value from the config
                    boolean exists = configHandler.exists(location);
                    Object configValue = configHandler.get(location);
                    List<String> configComments = configHandler.getComments(location);
                    List<String> configInlineComments = configHandler.getInlineComments(location);

                    if (!exists) {
                        configHandler.setDefault(location, defaultValue, defaultComments, defaultInlineComments);
                        didChange = true;
                    }

                    didChange = updateComments(didChange, location, defaultComments, defaultInlineComments, exists, configComments, configInlineComments);

                    //dont set if the value was just default was just set.
                    if (exists && defaultValue != configValue) {
                        validationHandler.checkValidation(field, configValue, this);

                        field.set(instance, configValue);
                    }
                    if (!accessible) field.setAccessible(false); //reset the field to its original state
                }
            }
        }

        if (didChange) configHandler.saveConfig();
    }

    public boolean checkSectionComments(Class<?> owningClass) {
        StringBuilder tempPrefix = new StringBuilder();
        boolean didChange = false;

        for (Class<?> clazz : getConfigurableClasses(owningClass)) { //includes self and parents
            //Remove the sepearator from the end of the prefix.
            String name = getLocation(clazz);
            String location = tempPrefix + name;

            tempPrefix.append(name).append(configHandler.separator()); //append the name

            List<String> defaultComments = ConfigUtils.getComments(clazz);
            List<String> defaultInlineComments = ConfigUtils.getInlineComments(clazz);

            boolean exists = configHandler.exists(location);
            List<String> configComments = configHandler.getComments(location);
            List<String> configInlineComments = configHandler.getInlineComments(location);

            //Reset comments in a config section overwriting all there.
            if (!exists) {
                configHandler.setDefaultSection(location, defaultComments, defaultInlineComments);
                didChange = true;
            }

            didChange = updateComments(didChange, location, defaultComments, defaultInlineComments, exists, configComments, configInlineComments);
        }

        return didChange;
    }

    public boolean updateComments(boolean didChange, String location, List<String> defaultComments, List<String> defaultInlineComments,
                                  boolean exists, List<String> configComments, List<String> configInlineComments) {
        if (exists && defaultComments != null && !defaultComments.equals(configComments)) {
            configHandler.setComments(location, defaultComments);
            didChange = true;
        }

        if (exists && defaultInlineComments != null && !defaultInlineComments.equals(configInlineComments)) {
            configHandler.setInlineComments(location, defaultInlineComments);
            didChange = true;
        }

        return didChange;
    }

    //README: Even know streams are slower we use them here for readability. (also not called that often)

    //Overrideable so you can change it to your own prefix provider.
    // This one does (Interface Class -> Super Class -> Declaring Class -> Class) recursive search optional.
    public String getPrefix(Class<?> clazz) {
        return getConfigurableClasses(clazz)
                .stream()
                .map(this::getLocation)
                .collect(Collectors.joining(configHandler.separator()));
    }

    //We use this to sort the classes so we dont have to sort with a if statement in the loop.
    public ArrayList<Class<?>> getConfigurableClasses(Class<?> clazz) {
        return getClasses(clazz)
                .stream()
                .filter(it -> it.isAnnotationPresent(Configurable.class))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //Reason this exists is because we also grab private fields. (declaredFields are public fields only)
    public ArrayList<Field> getFields(Class<?> owningClazz) {
        return getClasses(owningClazz).stream()
                .map(Class::getDeclaredFields)
                .flatMap(Arrays::stream)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    //use field or class name if empty
    public String getLocation(AnnotatedElement annotatedElement) {
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

    //(Interface Class -> Super Class -> Declaring Class -> Class) recursive search optional.
    public ArrayList<Class<?>> getClasses(Class<?> owningClazz) {
        ArrayList<Class<?>> classes = new ArrayList<>(); // unknown how to get rid of this without removing readability. TODO

        if (options.searchInterfaces) {
            classes.addAll(Arrays.asList(owningClazz.getInterfaces()));
        }

        if (options.searchSuperClasses) {
            for (Class<?> superClass = owningClazz.getSuperclass();
                 superClass != null;
                 superClass = superClass.getSuperclass()
            ) {
                classes.add(superClass);
            }
        }

        if (options.searchDeclaredClasses) {
            for (Class<?> declaringClass = owningClazz.getDeclaringClass();
                 declaringClass != null;
                 declaringClass = declaringClass.getDeclaringClass()
            ) {
                classes.add(declaringClass);
            }
        }

        if (options.searchReversibly && !classes.isEmpty()) { //dont create a new arraylist if we dont need to.
            ArrayList<Class<?>> newClasses = new ArrayList<>();

            for (Class<?> clazz : classes) {
                newClasses.addAll(getClasses(clazz));
            }

            classes = newClasses;
        }

        if (options.searchCurrentClass) {
            classes.add(owningClazz);
        }

        return classes;
    }
}
