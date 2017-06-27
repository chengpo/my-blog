package com.monkeyapp.blog.rest;

import com.monkeyapp.blog.module.PostRepository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/tags")
public class TagsResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getTags() {
        return new PostRepository().getPostTags();
    }
}
