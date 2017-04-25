package com.monkeyapp.blog.rest;

import com.monkeyapp.blog.rest.module.Post;
import com.monkeyapp.blog.rest.module.PostBank;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/posts")
public class PostsResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Post> getPostList() {
        return new PostBank().getPostList();
    }

    @GET @Path("/{tag}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Post> getPostListByTag(@PathParam("tag") String tag) {
        return new PostBank().getPostList(tag);
    }

    @GET @Path("/{year:\\d{4}}/{day:\\d{4}}/{time:\\d{4}}/{tag}/{title}")
    @Produces(MediaType.TEXT_HTML)
    public String getPostContent(@PathParam("year") String year,
                          @PathParam("day") String day,
                          @PathParam("time") String time,
                          @PathParam("tag") String tag,
                          @PathParam("title") String title) {

        Post post = new Post.Builder()
                            .setYear(year)
                            .setDay(day)
                            .setTime(time)
                            .setTag(tag)
                            .setTitle(title)
                            .build();

        return post.toString();
    }
}
