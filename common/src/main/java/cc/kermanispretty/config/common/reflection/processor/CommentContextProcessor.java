package cc.kermanispretty.config.common.reflection.processor;

import cc.kermanispretty.config.common.comment.annotation.Comment;
import cc.kermanispretty.config.common.comment.annotation.Footer;
import cc.kermanispretty.config.common.comment.annotation.Header;
import cc.kermanispretty.config.common.comment.annotation.InlineComment;
import cc.kermanispretty.config.common.location.context.LocationContext;
import cc.kermanispretty.config.common.reflection.context.CommentContext;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashSet;

public class CommentContextProcessor {

    public static void process(LinkedHashSet<LocationContext> locationContexts, AnnotatedElement element, String location, Object instance, boolean isInline) {
        // Comment annotations
        if (element.isAnnotationPresent(Comment.class)) {
            Comment comment = element.getAnnotation(Comment.class);
            String[] commentLines = comment.value();

            if (commentLines != null) {
                CommentContext.Type commentType = isInline ? CommentContext.Type.LOCATION : CommentContext.Type.SECTION;

                CommentContext commentContext = new CommentContext(location, instance, commentLines, commentType);

                locationContexts.add(commentContext);
            }
        }

        if (element.isAnnotationPresent(InlineComment.class)) { // TODO: fix this duplicate code.
            InlineComment inlineComment = element.getAnnotation(InlineComment.class);
            String[] commentLines = inlineComment.value();

            if (commentLines != null) {
                CommentContext.Type commentType = isInline ? CommentContext.Type.LOCATION_INLINE : CommentContext.Type.SECTION_INLINE;

                CommentContext commentContext = new CommentContext(location, instance, commentLines, commentType);

                locationContexts.add(commentContext);
            }
        }

        // Exit early for header/footer on a field.
        if (isInline) return;

        if (element.isAnnotationPresent(Header.class)) {
            Header header = element.getAnnotation(Header.class);
            String[] commentLines = header.value();

            if (commentLines != null) {
                CommentContext commentContext = new CommentContext(location, instance, commentLines, CommentContext.Type.HEADER);

                locationContexts.add(commentContext);
            }
        }

        if (element.isAnnotationPresent(Footer.class)) {
            Footer footer = element.getAnnotation(Footer.class);
            String[] commentLines = footer.value();

            if (commentLines != null) {
                CommentContext commentContext = new CommentContext(location, instance, commentLines, CommentContext.Type.FOOTER);

                locationContexts.add(commentContext);
            }
        }
    }
}
