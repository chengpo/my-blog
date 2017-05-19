package com.monkeyapp.blog.rest.utils;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;

public class TextReader implements Reader {
    private final int maxLines;
    private final String path;

    public static TextReader partialReader(String path) {
        return new TextReader(path, 10);
    }

    public static TextReader fullReader(String path) {
        return new TextReader(path, -1);
    }

    private TextReader(String path, int maxLines) {
        this.path = path;
        this.maxLines = maxLines;
    }

    @Override
    public String read() {
        return Optional.ofNullable(Thread.currentThread()
                                    .getContextClassLoader()
                                    .getResource(path))
                .flatMap((url) -> {
                    try {
                        final String content = (maxLines <= 0) ?
                                new String(Files.readAllBytes(Paths.get(URI.create(url.toString())))):
                                Files.lines(Paths.get(URI.create(url.toString())))
                                        .limit(maxLines)
                                        .collect(Collectors.joining(System.lineSeparator()));

                        return Optional.of(content);
                    } catch (IOException e) {
                        throw new WebApplicationException(e);
                    }
                })
                .orElseThrow(() -> new WebApplicationException(404));
    }
}
