package com.monkeyapp.blog.rest;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

@Path("/status")
public class StatusResource {
    private static final Logger LOG = Logger.getLogger(StatusResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@Context HttpHeaders headers) {
        LOG.debug("Enter get()");
        return new Gson().toJson(headers.getRequestHeaders());
    }
}
