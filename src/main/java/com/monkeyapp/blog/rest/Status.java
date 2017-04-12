package com.monkeyapp.blog.rest;

import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/status")
public class Status {
    private static final Logger LOG = Logger.getLogger(Status.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getStatus() {
        LOG.debug("Enter getStatus");
        return "{Hello World from Tomcat Embedded with Jersey!}";
    }
}
