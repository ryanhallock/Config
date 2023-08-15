package cc.kermanispretty.config.common;

import cc.kermanispretty.config.common.reflection.context.CommentContext;
import cc.kermanispretty.config.common.reflection.context.FieldContext;

import java.util.List;

public interface ConfigHandler {

    String separator();

    void loadConfig();

    void saveConfig();

    boolean exists(String location);

    Object get(FieldContext context);

    List<String> getComment(CommentContext commentContext);

    void setComment(CommentContext commentContext);

    void set(FieldContext context, Object value);

    boolean shouldUpdateComments(CommentContext context);
}
