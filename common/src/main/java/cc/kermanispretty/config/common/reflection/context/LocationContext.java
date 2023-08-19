package cc.kermanispretty.config.common.reflection.context;

public abstract class LocationContext {
    protected final String location;
    protected final Object instance;

    public LocationContext(String location, Object instance) {
        this.location = location;
        this.instance = instance;
    }

    public Class<?> getOwningClass() {
        return instance.getClass();
    }

    public String getLocation() {
        return location;
    }
}
