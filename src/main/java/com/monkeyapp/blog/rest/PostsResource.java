package com.monkeyapp.blog.rest;

import com.monkeyapp.blog.rest.module.PostHead;
import com.monkeyapp.blog.rest.module.PostBank;
import com.monkeyapp.blog.rest.module.PostEntity;
import com.monkeyapp.blog.rest.module.PostReader;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/posts")
public class PostsResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PostHead> getPosts(@DefaultValue("") @QueryParam("tag") String tag) {
        return new PostBank().getPosts(tag);
    }

    @GET @Path("/{year:\\d{4}}/{day:\\d{4}}/{time:\\d{4}}/{tag}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public PostEntity getPostContent(@PathParam("year") String year,
                                     @PathParam("day") String day,
                                     @PathParam("time") String time,
                                     @PathParam("tag") String tag,
                                     @PathParam("title") String title) {
        PostHead post = new PostHead.Builder()
                            .setYear(year)
                            .setDay(day)
                            .setTime(time)
                            .setTag(tag)
                            .setTitle(title)
                            .build();

        return new PostEntity(post, new PostReader(post).toHtml());
    }
}