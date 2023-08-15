package cc.kermanispretty.config.common.reflection.context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

public class FieldContext extends LocationContext {
    private static Field modifiersField;

    static {
        try {
            Method declaredFields = Class.class.getDeclaredMethod("getDeclaredFields0", boolean.class);
            declaredFields.setAccessible(true);
            Field[] fields = (Field[]) declaredFields.invoke(Field.class, false);
            modifiersField = Arrays.stream(fields)
                    .filter(field -> field.getName().equals("modifiers"))
                    .findFirst()
                    .orElse(null);
            if (modifiersField != null)
                modifiersField.setAccessible(true);
        } catch (Exception | ExceptionInInitializerError e) {
            System.err.println("Failed to get modifiers field. If you are using JDK 11-17 use:" +
                    "\n --add-opens java.base/java.lang=ALL-UNNAMED --add-opens java.base/java.lang.reflect=ALL-UNNAMED" +
                    "\n to your JVM arguments to fix this issue. Otherwise, static config fields will be unsupported." +
                    "\n This might still not work due more recent versions of javac inlining finals at compile time."
            );
        }
    }

    private final Field field;
    private final boolean isAccessible;
    private final boolean isFinal;
    private boolean pushed = false;

    public FieldContext(Field field, String location, Object instance) {
        super(location, instance);
        this.field = field;
        this.isAccessible = field.isAccessible();
        this.isFinal = Modifier.isFinal(field.getModifiers());
    }

    // unlocks the field to be set.
    public void push() throws IllegalAccessException {
        field.setAccessible(true);
        if (modifiersField != null)
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        this.pushed = true;
    }

    public void pop() throws IllegalAccessException {
        field.setAccessible(isAccessible);
        if (modifiersField != null && isFinal)
            modifiersField.setInt(field, field.getModifiers() | Modifier.FINAL);

        this.pushed = false;
    }

    public void set(Object value) throws IllegalAccessException {
        if (!pushed)
            throw new IllegalAccessException("FieldContext is not pushed.");
        if (isFinal && modifiersField == null)
            return;

        field.set(instance, value);
    }

    // Used to figure out to not write this into config.
    public boolean isTransient() {
        return Modifier.isTransient(field.getModifiers());
    }

    public boolean isStatic() {
        return Modifier.isStatic(field.getModifiers());
    }

    public Class<?> getOwningClass() {
        return field.getDeclaringClass();
    }

    public Object getValue() throws IllegalAccessException {
        return field.get(instance);
    }

    public Field getField() {
        return field;
    }
}
