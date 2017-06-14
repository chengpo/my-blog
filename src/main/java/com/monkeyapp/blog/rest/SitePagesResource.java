package com.monkeyapp.blog.rest;

import com.monkeyapp.blog.rest.module.Entity;
import com.monkeyapp.blog.rest.utils.MarkdownReader;
import com.monkeyapp.blog.rest.module.Post;
import com.monkeyapp.blog.rest.utils.TextReader;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.Optional;

@Path("/site-pages")
public class SitePagesResource {
    @GET @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post getSitePage(@PathParam("name") String name) {
        return Optional.ofNullable(Entity.fromFileName(name))
                .flatMap((entity) ->
                    Optional.of(new Post(entity,
                                new MarkdownReader(
                                        TextReader.fullReader("site_pages" + File.separator + name)).read())))
                .orElseThrow(() -> new WebApplicationException(404));
    }
}
