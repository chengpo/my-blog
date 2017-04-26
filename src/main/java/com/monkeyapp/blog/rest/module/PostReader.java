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

public class PostReader {
    private static final Logger LOG = Logger.getLogger(PostReader.class);

    private final PostHead post;

    public PostReader(PostHead post) {
        this.post = post;
    }

    public String toHtml() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(loadContent());
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return renderer.render(document);
    }

    private static Optional<String> readFile(URL url) {
        try {
            return Optional.of(
                    new String(
                            Files.readAllBytes(Paths.get(URI.create(url.toString())))));
        } catch (IOException e) {
            LOG.error("failed to read post " + url.toString(), e);
            throw new WebApplicationException(e);
        }
    }

    private String loadContent() {
        final String postPath = "posts/"+post.toFileName();
        Optional<URL> urlOptional = Optional.ofNullable(Thread.currentThread()
                                                            .getContextClassLoader()
                                                            .getResource(postPath));

        return urlOptional.flatMap(PostReader::readFile)
                   .orElseThrow(() -> new WebApplicationException(404));
    }
}
