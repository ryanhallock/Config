package cc.kermanispretty.config.bukkit;

import cc.kermanispretty.config.common.ConfigHandler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BukkitConfigHandler implements ConfigHandler {
    private final File configPath;
    private YamlConfiguration configuration;

    public BukkitConfigHandler(String configFolder, String configName) {
        this.configPath = new File(configFolder, configName);
    }

    @Override
    public String separator() {
        return ".";
    }

    @Override
    public void loadConfig() {
        configuration = YamlConfiguration.loadConfiguration(configPath);
    }

    @Override
    public void saveConfig() {
        try {
            configuration.save(configPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(String location) {
        return configuration.contains(location);
    }

    @Override
    public Object get(String location) {
        return configuration.get(location);
    }

    @Override
    public List<String> getComments(String location) {
        return configuration.getComments(location);
    }

    @Override
    public List<String> getInlineComments(String location) {
        return configuration.getInlineComments(location);
    }

    @Override
    public void setComments(String location, List<String> comments) {
        configuration.setComments(location, comments);
    }

    @Override
    public void setInlineComments(String location, List<String> comments) {
        configuration.setInlineComments(location, comments);
    }

    @Override
    public void setDefault(String location, Object data, List<String> comments, List<String> inlineComments) {
        configuration.set(location, data);
        configuration.setComments(location, comments);
        configuration.setInlineComments(location, inlineComments);
    }

    @Override
    public void setDefaultSection(String location, List<String> comments, List<String> inlineComments) {
        if (!configuration.isConfigurationSection(location)) configuration.createSection(location);

        configuration.setComments(location, comments);
        configuration.setInlineComments(location, inlineComments);
    }

    @Override
    public List<String> getHeader() {
        return configuration.options().getHeader();
    }

    @Override
    public void setHeader(List<String> header) {
        configuration.options().setHeader(header);
    }

    @Override
    public List<String> getFooter() {
        return configuration.options().getFooter();
    }

    @Override
    public void setFooter(List<String> footer) {
        configuration.options().setFooter(footer);
    }

}
