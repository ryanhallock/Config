package cc.kermanispretty.config.common;

import cc.kermanispretty.config.common.annotation.Configurable;
import cc.kermanispretty.config.common.annotation.comment.Comment;
import cc.kermanispretty.config.common.annotation.comment.Footer;
import cc.kermanispretty.config.common.annotation.comment.Header;
import cc.kermanispretty.config.common.annotation.comment.InlineComment;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class Config {
    private ConfigHandler configHandler;
    private ConfigOptions options;
    private Set<Object> registeredObjects;

    public Config(ConfigHandler configHandler, ConfigOptions options) {
        this.configHandler = configHandler;
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
        registeredObjects.clear();
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


            List<String> defaultHeader = owningClass.isAnnotationPresent(Header.class)
                    ? Arrays.stream(owningClass.getAnnotation(Header.class).value()).collect(Collectors.toList())
                    : null;
            List<String> defaultFooter = owningClass.isAnnotationPresent(Footer.class)
                    ? Arrays.stream(owningClass.getAnnotation(Footer.class).value()).collect(Collectors.toList())
                    : null;

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

            //Recersive check for all comments sections.
            didChange |= checkSectionComments(owningClass);

            for (Field field : getFields(owningClass)) {
                if (field.isAnnotationPresent(Configurable.class)) {
                    boolean accessible = field.isAccessible();

                    if (!accessible) field.setAccessible(true);

                    //grab the default value from the field
                    Object defaultValue = field.get(instance);

                    //grab annotation values
                    String location = prefix + field.getAnnotation(Configurable.class).value();

                    List<String> defaultComments = field.isAnnotationPresent(Comment.class)
                            ? Arrays.stream(field.getAnnotation(Comment.class).value()).collect(Collectors.toList())
                            : null;
                    List<String> defaultInlineComments = field.isAnnotationPresent(InlineComment.class)
                            ? Arrays.stream(field.getAnnotation(InlineComment.class).value()).collect(Collectors.toList())
                            : null;

                    //grab the value from the config
                    boolean exists = configHandler.exists(location);
                    Object configValue = configHandler.get(location);
                    List<String> configComments = configHandler.getComments(location);
                    List<String> configInlineComments = configHandler.getInlineComments(location);

                    if (!exists) {
                        configHandler.setDefault(location, defaultValue, defaultComments, defaultInlineComments);
                        didChange = true;
                    }

                    if (exists && defaultComments != null && !defaultComments.equals(configComments)) {
                        configHandler.setComments(location, defaultComments);
                        didChange = true;
                    }

                    if (exists && defaultInlineComments != null && !defaultInlineComments.equals(configInlineComments)) {
                        configHandler.setInlineComments(location, defaultInlineComments);
                        didChange = true;
                    }

                    //dont set if the value was just default was just set.
                    if (exists && defaultValue != configValue) field.set(instance, configValue);
                    if (!accessible) field.setAccessible(false); //reset the field to its original state
                }
            }
        }

        if (didChange) configHandler.saveConfig();
    }

    public boolean checkSectionComments(Class<?> owningClass) {
        StringBuilder tempPrefix = new StringBuilder();
        for (Class<?> clazz : getConfigurableClasses(owningClass)) { //includes self and parents
            //Remove the sepearator from the end of the prefix.
            String location = tempPrefix + clazz.getAnnotation(Configurable.class).value();

            tempPrefix.append(location).append(configHandler.separator());

            List<String> defaultComments = clazz.isAnnotationPresent(Comment.class)
                    ? Arrays.stream(clazz.getAnnotation(Comment.class).value()).collect(Collectors.toList())
                    : null;
            List<String> defaultInlineComments = clazz.isAnnotationPresent(InlineComment.class)
                    ? Arrays.stream(clazz.getAnnotation(InlineComment.class).value()).collect(Collectors.toList())
                    : null;

            boolean exists = configHandler.exists(location);
            List<String> configComments = configHandler.getComments(location);
            List<String> configInlineComments = configHandler.getInlineComments(location);

            //Reset comments in a config section overwriting all there.
            if (!exists
                    || (defaultComments != null && !defaultComments.equals(configComments))
                    || (defaultInlineComments != null && !defaultInlineComments.equals(configInlineComments))
            ) {
                configHandler.setDefaultSection(location, defaultComments, defaultInlineComments);
                return true;
            }
        }

        return false;
    }

    //README: Even know streams are slower we use them here for readability. (also not called that often)

    //Overrideable so you can change it to your own prefix provider.
    // This one does (Interface Class -> Super Class -> Declaring Class -> Class) recursive search optional.
    public String getPrefix(Class<?> clazz) {
        return getConfigurableClasses(clazz)
                .stream()
                .map(it -> it.getAnnotation(Configurable.class).value())
                .collect(Collectors.joining(configHandler.separator()));
    }

    //We use this to sort the classes so we dont have to sort with a if statement in the loop.
    public Set<Class<?>> getConfigurableClasses(Class<?> clazz) {
        return getClasses(clazz)
                .stream()
                .filter(c -> c.isAnnotationPresent(Configurable.class))
                .collect(Collectors.toSet());
    }

    //Reason this exists is because we also grab private fields. (declaredFields are public fields only)
    public Set<Field> getFields(Class<?> owningClazz) {
        return getClasses(owningClazz).stream()
                .map(Class::getDeclaredFields)
                .flatMap(Arrays::stream)
                .collect(Collectors.toSet());
    }

    //(Interface Class -> Super Class -> Declaring Class -> Class) recursive search optional.
    public Set<Class<?>> getClasses(Class<?> owningClazz) {
        HashSet<Class<?>> classes = new HashSet<>(); // unknown how to get rid of this without removing readability. TODO

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

        //Expermental: TODO
        if (options.searchReversibly && !classes.isEmpty()) { //dont create a new hashset if we dont need to.
            HashSet<Class<?>> newClasses = new HashSet<>();

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
