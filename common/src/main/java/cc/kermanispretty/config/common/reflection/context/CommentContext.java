package cc.kermanispretty.config.common.reflection.context;

import cc.kermanispretty.config.common.location.context.LocationContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommentContext extends LocationContext {
    private final String[] comments;
    private final Type type;

    public CommentContext(String location, Object instance, String[] comments, Type type) {
        super(location, instance);

        this.comments = comments;
        this.type = type;
    }

    public String[] getCommentArray() {
        return comments;
    }

    public List<String> getCommentList() {
        return Arrays.asList(this.comments);
    }

    public List<String> getCommentListSplitFix() {
        return Arrays.stream(comments) // we need to split \n to a null value and keep it.
                .flatMap(line -> Arrays.stream(line.split("((?=\\n))"))) // keep starting \n
                .map(line -> line.replaceAll("\\n", "")) // replace extra starting \n
                .map(line -> line.replaceAll(" +$", "")) //java 8 solution for trimming trailing spaces (java 11+ has this function built in)
                .map(line -> line.isEmpty() ? null : line)
                .flatMap(line -> line == null ? Stream.of("", null) : Stream.of(line)) // some strange config fix for yaml (seems to create some strange spacing though)
                .collect(Collectors.toList());
    }

    public Type getType() {
        return type;
    }

    public boolean isField() {
        return type == Type.LOCATION || type == Type.LOCATION_INLINE;
    }

    public enum Type {
        HEADER,
        FOOTER,
        LOCATION, // Used for fields
        LOCATION_INLINE,
        SECTION, // Used for classes
        SECTION_INLINE
    }
}
