package cc.kermanispretty.config.bukkit;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.ConfigHandler;
import cc.kermanispretty.config.common.ConfigOptionEnum;
import cc.kermanispretty.config.common.transform.TransformerHandler;
import cc.kermanispretty.config.common.validation.ValidationHandler;

import java.util.EnumSet;

public class BukkitConfig extends Config {

    public BukkitConfig(ConfigHandler configHandler, ValidationHandler validationHandler, TransformerHandler transformerHandler, EnumSet<ConfigOptionEnum> options) {
        super(configHandler, validationHandler, transformerHandler, options);
    }
}
