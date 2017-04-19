package com.monkeyapp.blog.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Path("/status")
public class StatusResource {
    @GET
    public Response get(@Context HttpHeaders headers) {
            Optional<URL> fileListUrl = Optional.ofNullable(Thread.currentThread()
                                                .getContextClassLoader()
                                                .getResource("posts/file-list.json"));

            return fileListUrl.flatMap(StatusResource::dumpPostList)
                              .orElseGet(StatusResource::errorResponse);
    }

    private static Optional<Response> dumpPostList(URL url) {
       try {
            return Optional.of(
                    Response.ok(new String(
                                        Files.readAllBytes(Paths.get(url.toURI())),
                                        StandardCharsets.UTF_8),
                                MediaType.APPLICATION_JSON)
                            .build());

        } catch (IOException | URISyntaxException e) {
            return Optional.empty();
        }
    }

    private static Response errorResponse() {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Post list not found")
                .type(MediaType.TEXT_PLAIN)
                .build();
    }
}
