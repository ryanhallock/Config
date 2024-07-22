package cc.kermanispretty.config.bukkit;

import cc.kermanispretty.config.bukkit.processor.ConfigurableValueProcessor;
import cc.kermanispretty.config.common.ConfigHandler;
import cc.kermanispretty.config.common.reflection.context.CommentContext;
import cc.kermanispretty.config.common.reflection.context.FieldContext;
import org.bukkit.configuration.file.YamlConfiguration;

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
            throw new RuntimeException(String.format("Failed to save annotation based config `%s`", configPath.getName()), e);
        }
    }

    @Override
    public boolean exists(String location) {
        return configuration.contains(location);
    }

    @Override
    public Object get(FieldContext context) {
        return configuration.getObject(context.getLocation(), context.getField().getType());
    }

    @Override
    public List<String> getComment(CommentContext commentContext) {
        if (!exists(commentContext.getLocation())) return null;

        switch (commentContext.getType()) {
            case LOCATION:
            case SECTION:
                return configuration.getComments(commentContext.getLocation());
            case LOCATION_INLINE:
            case SECTION_INLINE:
                return configuration.getInlineComments(commentContext.getLocation());
            case HEADER:
                return configuration.options().getHeader();
            case FOOTER:
                return configuration.options().getFooter();
            default:
                throw new IllegalStateException("Unexpected value: " + commentContext.getType());
        }
    }

    @Override
    public void setComment(CommentContext commentContext) {
        if (!exists(commentContext.getLocation()) && !commentContext.isField())
            configuration.createSection(commentContext.getLocation());

        List<String> comments = commentContext.getCommentListSplitFix();

        switch (commentContext.getType()) {
            case LOCATION:
            case SECTION:
                configuration.setComments(commentContext.getLocation(), comments);
                break;
            case LOCATION_INLINE:
            case SECTION_INLINE:
                configuration.setInlineComments(commentContext.getLocation(), comments);
                break;
            case HEADER:
                configuration.options().setHeader(comments);
                break;
            case FOOTER:
                configuration.options().setFooter(comments);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + commentContext.getType());
        }
    }

    @Override
    public void set(FieldContext context, Object value) {
        try {
            configuration.set(context.getLocation(), ConfigurableValueProcessor.process(value));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override // All of these should exist already
    public boolean shouldUpdateComments(CommentContext context) {
        List<String> splitFixComments = context.getCommentListSplitFix();
        List<String> comments = getComment(context);

        if (comments == null) return true;

        if (splitFixComments != null) {
            return !comments.equals(splitFixComments);
        }

        return false;
    }
}
