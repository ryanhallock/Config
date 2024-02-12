package cc.kermanispretty.config.bukkit.builder;

import cc.kermanispretty.config.bukkit.BukkitConfig;
import cc.kermanispretty.config.bukkit.BukkitConfigHandler;
import cc.kermanispretty.config.bukkit.transformer.annotation.Colored;
import cc.kermanispretty.config.common.ConfigHandler;
import cc.kermanispretty.config.common.ConfigOptionEnum;
import cc.kermanispretty.config.common.transform.TransformerHandler;
import cc.kermanispretty.config.common.validation.ValidatorHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;

public class BukkitConfigBuilder {
    private String path;
    private String fileName;
    private ConfigHandler handler;
    private ValidatorHandler validatorHandler;
    private TransformerHandler transformerHandler;
    private EnumSet<ConfigOptionEnum> options;
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

    public BukkitConfigBuilder options(EnumSet<ConfigOptionEnum> options) {
        this.options = options;
        return this;
    }

    public BukkitConfigBuilder register(Object... objects) {
        register.addAll(Arrays.asList(objects));
        return this;
    }

    public BukkitConfigBuilder registerValidator(Class<? extends Annotation> clazz) {
        if (validatorHandler == null) {
            validatorHandler = new ValidatorHandler(ValidatorHandler.DEFAULT_IMPL);
        }

        validatorHandler.register(clazz);

        return this;
    }

    public BukkitConfigBuilder registerTransformer(Class<? extends Annotation> clazz) {
        if (transformerHandler == null) {
            transformerHandler = new TransformerHandler(Colored.class);
        }

        transformerHandler.register(clazz);

        return this;
    }


    public BukkitConfigBuilder transformer(TransformerHandler transformerHandler) {
        this.transformerHandler = transformerHandler;
        return this;
    }

    public BukkitConfigBuilder validator(ValidatorHandler validatorHandler) {
        this.validatorHandler = validatorHandler;
        return this;
    }

    public BukkitConfig build() {
        if (handler == null) {
            handler = new BukkitConfigHandler(path, fileName);
        }

        if (options == null)
            options = ConfigOptionEnum.getDefaultOptions();

        if (validatorHandler == null)
            validatorHandler = new ValidatorHandler(ValidatorHandler.DEFAULT_IMPL);

        if (transformerHandler == null)
            transformerHandler = new TransformerHandler(Colored.class);

        BukkitConfig config = new BukkitConfig(handler, validatorHandler, transformerHandler, options);

        try {
            config.register(register.toArray());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return config;
    }

    public BukkitConfig buildAndLoad() {
        BukkitConfig config = build();

        try {
            config.load();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return config;
    }
}
