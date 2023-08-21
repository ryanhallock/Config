package cc.kermanispretty.config.bukkit;

import cc.kermanispretty.config.common.Config;
import cc.kermanispretty.config.common.ConfigHandler;
import cc.kermanispretty.config.common.ConfigOptionEnum;
import cc.kermanispretty.config.common.transform.TransformerHandler;
import cc.kermanispretty.config.common.validation.ValidatorHandler;

import java.util.EnumSet;

public class BukkitConfig extends Config {

    public BukkitConfig(ConfigHandler configHandler, ValidatorHandler validatorHandler, TransformerHandler transformerHandler, EnumSet<ConfigOptionEnum> options) {
        super(configHandler, validatorHandler, transformerHandler, options);
    }
}
