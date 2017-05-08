package com.monkeyapp.blog.rest;

import com.monkeyapp.blog.rest.module.Post;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/site-pages")
public class SitePagesResource {

    @GET @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post getSitePage(@PathParam("name") String name) {

        return null;
    }
}
