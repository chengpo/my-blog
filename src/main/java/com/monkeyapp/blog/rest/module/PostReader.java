package com.monkeyapp.blog.rest.module;

import org.apache.log4j.Logger;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;

public class PostReader {
    private static final Logger LOG = Logger.getLogger(PostReader.class);

    private final PostHead post;

    public static PostEntity toFullEntity(PostHead post) {
        return new PostEntity(post, toHtmlContent(post, 0));
    }

    public static PostEntity toBriefEntity(PostHead post) {
        return new PostEntity(post, toHtmlContent(post, 30));
    }

    private static String toHtmlContent(PostHead post, int maxSize) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(new PostReader(post).loadContent(maxSize));
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    private PostReader(PostHead post) {
        this.post = post;
    }

    private String loadContent(int maxSize) {
        final String filePath = "posts/"+post.toFileName();
        Optional<URL> urlOptional = Optional.ofNullable(Thread.currentThread()
                                                            .getContextClassLoader()
                                                            .getResource(filePath));

        return urlOptional.flatMap((url) -> readFile(url, maxSize))
                   .orElseThrow(() -> new WebApplicationException(404));
    }

    private Optional<String> readFile(URL url, int maxSize) {
        try {
            String content = (maxSize <= 0) ?
                    new String(Files.readAllBytes(Paths.get(URI.create(url.toString())))):
                    Files.lines(Paths.get(URI.create(url.toString())))
                         .limit(maxSize)
                         .collect(Collectors.joining(System.lineSeparator()));

            return Optional.of(content);
        } catch (IOException e) {
            LOG.error("failed to read post " + url.toString(), e);
            throw new WebApplicationException(e);
        }
    }
}
