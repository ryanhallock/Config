package cc.kermanispretty.config.common.reflection.processor;

import cc.kermanispretty.config.common.annotation.comment.Comment;
import cc.kermanispretty.config.common.annotation.comment.Footer;
import cc.kermanispretty.config.common.annotation.comment.Header;
import cc.kermanispretty.config.common.annotation.comment.InlineComment;
import cc.kermanispretty.config.common.reflection.context.CommentContext;
import cc.kermanispretty.config.common.reflection.context.LocationContext;

import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashSet;

public class CommentContextProcessor {

    public static void process(LinkedHashSet<LocationContext> locationContexts, AnnotatedElement element, String location, Object instance, boolean isField) {
        // Comment annotations
        if (element.isAnnotationPresent(Comment.class)) {
            Comment comment = element.getAnnotation(Comment.class);
            String[] commentLines = comment.value();

            if (commentLines != null) {
                CommentContext.CommentType commentType = isField ? CommentContext.CommentType.LOCATION : CommentContext.CommentType.SECTION;

                CommentContext commentContext = new CommentContext(location, instance, commentLines, commentType);

                locationContexts.add(commentContext);
            }
        }

        if (element.isAnnotationPresent(InlineComment.class)) { // TODO: fix this duplicate code.
            InlineComment inlineComment = element.getAnnotation(InlineComment.class);
            String[] commentLines = inlineComment.value();

            if (commentLines != null) {
                CommentContext.CommentType commentType = isField ? CommentContext.CommentType.LOCATION_INLINE : CommentContext.CommentType.SECTION_INLINE;

                CommentContext commentContext = new CommentContext(location, instance, commentLines, commentType);

                locationContexts.add(commentContext);
            }
        }

        // Exit early for header/footer on a field.
        if (isField) return;

        if (element.isAnnotationPresent(Header.class)) {
            Header header = element.getAnnotation(Header.class);
            String[] commentLines = header.value();

            if (commentLines != null) {
                CommentContext commentContext = new CommentContext(location, instance, commentLines, CommentContext.CommentType.HEADER);

                locationContexts.add(commentContext);
            }
        }

        if (element.isAnnotationPresent(Footer.class)) {
            Footer footer = element.getAnnotation(Footer.class);
            String[] commentLines = footer.value();

            if (commentLines != null) {
                CommentContext commentContext = new CommentContext(location, instance, commentLines, CommentContext.CommentType.FOOTER);

                locationContexts.add(commentContext);
            }
        }
    }
}
