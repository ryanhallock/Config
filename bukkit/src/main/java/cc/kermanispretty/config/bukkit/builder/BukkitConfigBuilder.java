package cc.kermanispretty.config.bukkit.builder;

import cc.kermanispretty.config.bukkit.BukkitConfig;
import cc.kermanispretty.config.bukkit.BukkitConfigHandler;
import cc.kermanispretty.config.common.ConfigHandler;
import cc.kermanispretty.config.common.ConfigOptions;
import cc.kermanispretty.config.common.validation.ValidationHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;

public class BukkitConfigBuilder {
    private String path;
    private String fileName;
    private ConfigHandler handler;
    private ValidationHandler validationHandler = ValidationHandler.DEFAULT;
    private ConfigOptions options = ConfigOptions.DEFAULT;
    private final HashSet<Object> register = new HashSet<>();

    public BukkitConfigBuilder(JavaPlugin plugin, String fileName) {
        this.path = plugin.getDataFolder().getAbsolutePath();
        this.fileName = fileName;
    }

    public BukkitConfigBuilder(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
    }

    public BukkitConfigBuilder(JavaPlugin plugin) {
        this.path = plugin.getDataFolder().getAbsolutePath();
    }

    public BukkitConfigBuilder path(String path) {
        this.path = path;
        return this;
    }

    public BukkitConfigBuilder filename(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public BukkitConfigBuilder options(ConfigOptions options) {
        this.options = options;
        return this;
    }

    public BukkitConfigBuilder register(Object... objects) {
        register.addAll(Arrays.asList(objects));
        return this;
    }

    public BukkitConfigBuilder validationHandler(ValidationHandler validationHandler) {
        this.validationHandler = validationHandler;
        return this;
    }

    public BukkitConfig build() {
        if (handler == null) {
            handler = new BukkitConfigHandler(path, fileName);
        }

        BukkitConfig config = new BukkitConfig(handler, validationHandler, options);

        config.register(register);

        return config;
    }

    public BukkitConfig registerAndLoad(Object... objects) {
        BukkitConfig config = build();

        config.register(register);
        config.register(objects);
        config.load();

        return config;
    }
}
