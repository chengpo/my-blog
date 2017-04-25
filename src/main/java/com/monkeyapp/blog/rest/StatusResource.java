package com.monkeyapp.blog.rest;

import com.monkeyapp.blog.rest.module.PostBank;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Path("/status")
public class StatusResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getStatus(@Context HttpHeaders headers) {
        return new PostBank().toString();
    }
}
