package com.monkeyapp.blog.rest;

import com.monkeyapp.blog.rest.module.PostHead;
import com.monkeyapp.blog.rest.module.PostBank;
import com.monkeyapp.blog.rest.module.PostEntity;
import com.monkeyapp.blog.rest.module.PostReader;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/posts")
public class PostsResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PostEntity> getPostList(@DefaultValue("") @QueryParam("tag") String tag) {
        return new PostBank()
                        .getPosts(tag)
                        .stream()
                        .map(PostReader::toBriefEntity)
                        .collect(Collectors.toList());
    }

    @GET @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public PostEntity getPostDetail(@PathParam("name") String name) {
        return Optional.ofNullable(PostHead.from(name + ".md"))
                       .flatMap((head)-> Optional.of(PostReader.toFullEntity(head)))
                       .orElseThrow(() -> new WebApplicationException(404));
    }
}
