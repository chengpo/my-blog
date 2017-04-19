package com.monkeyapp.blog.rest;

import com.monkeyapp.blog.rest.module.PostBank;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/status")
public class StatusResource {
    @GET
    public Response get(@Context HttpHeaders headers) {
        return Response.ok(new PostBank().toString(), MediaType.TEXT_PLAIN).build();
    }
}
