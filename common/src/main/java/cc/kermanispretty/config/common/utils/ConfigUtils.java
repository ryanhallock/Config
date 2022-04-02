package cc.kermanispretty.config.common.utils;

import cc.kermanispretty.config.common.annotation.comment.Comment;
import cc.kermanispretty.config.common.annotation.comment.Footer;
import cc.kermanispretty.config.common.annotation.comment.Header;
import cc.kermanispretty.config.common.annotation.comment.InlineComment;

import java.lang.reflect.AnnotatedElement;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigUtils {
    public static List<String> getComments(AnnotatedElement object) {
        return object.isAnnotationPresent(Comment.class)
                ? splitFix(object.getAnnotation(Comment.class).value())
                : null;
    }

    public static List<String> getInlineComments(AnnotatedElement object) {
        return object.isAnnotationPresent(InlineComment.class)
                ? splitFix(object.getAnnotation(InlineComment.class).value())
                : null;
    }

    public static List<String> getUnsplitComments(AnnotatedElement object) {
        return object.isAnnotationPresent(Comment.class)
                ? Arrays.stream(object.getAnnotation(Comment.class).value()).collect(Collectors.toList())
                : null;
    }

    public static List<String> getUnsplitInlineComments(AnnotatedElement object) {
        return object.isAnnotationPresent(InlineComment.class)
                ? Arrays.stream(object.getAnnotation(InlineComment.class).value()).collect(Collectors.toList())
                : null;
    }

    public static List<String> getHeader(Class<?> object) {
        return object.isAnnotationPresent(Header.class)
                ? splitFix(object.getAnnotation(Header.class).value())
                : null;
    }

    public static List<String> getFooter(Class<?> object) {
        return object.isAnnotationPresent(Footer.class)
                ? splitFix(object.getAnnotation(Footer.class).value())
                : null;
    }

    public static List<String> splitFix(String[] lines) {
        return Arrays.stream(lines) // we need to split \n to a null value and keep it.
                .flatMap(line -> Arrays.stream(line.split("((?=\\n))"))) // keep starting \n
                .map(line -> line.replaceAll("\\n", "")) // replace extra starting \n
                .map(line -> line.replaceAll(" +$", "")) //java 8 solution for trimming trailing spaces (java 11+ has this function built in)
                .map(line -> line.isEmpty() ? null : line)
                .flatMap(line -> line == null ? Stream.of("", null) : Stream.of(line)) // some strange config fix for yaml (seems to create some strange spacing though)
                .collect(Collectors.toList());
    }

}
