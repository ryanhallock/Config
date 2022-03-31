package cc.kermanispretty.config.common;

import java.util.List;

public interface ConfigHandler {

    String separator();

    void loadConfig();

    void saveConfig();

    boolean exists(String location);

    Object get(String location);

    List<String> getComments(String location);

    List<String> getInlineComments(String location);

    void setComments(String location, List<String> comments);

    void setInlineComments(String location, List<String> comments);

    //Will be called if value is missing from config to generate it.
    void setDefault(String location, Object data, List<String> comments, List<String> inlineComments);

    //For section.
    void setDefaultSection(String location, List<String> comments, List<String> inlineComments);

    List<String> getHeader();
    List<String> getFooter();

    void setHeader(List<String> header);
    void setFooter(List<String> footer);
}
