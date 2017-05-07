package com.monkeyapp.blog.rest;

import com.monkeyapp.blog.rest.module.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/posts")
public class PostsResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Paper> getPostList(@DefaultValue("") @QueryParam("tag") String tag) {
        return new PostBank()
                        .getPosts(tag)
                        .stream()
                        .map((entity) -> {
                                final String path = "posts/" + entity.getName() + ".md";
                                final String content = new MarkdownReader(
                                                                TextReader.partialReader(path)).read();
                                return new Paper(entity, content);
                            }
                        )
                        .collect(Collectors.toList());
    }

    @GET @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Paper getPostDetail(@PathParam("name") String name) {
        return Optional.ofNullable(Entity.from(name + ".md"))
                       .flatMap((entity)-> {
                                    final String path = "posts/" + entity.getName() + ".md";
                                    final String content = new MarkdownReader(
                                                                TextReader.fullReader(path)).read();
                                    return Optional.of(new Paper(entity, content));
                               }
                       )
                       .orElseThrow(() -> new WebApplicationException(404));
    }
}
