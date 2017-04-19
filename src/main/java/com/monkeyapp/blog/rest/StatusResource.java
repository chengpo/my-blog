package com.monkeyapp.blog.rest;

import com.google.gson.Gson;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Path("/status")
public class StatusResource {
    private static final Logger LOG = Logger.getLogger(StatusResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@Context HttpHeaders headers) {
        LOG.debug("Enter get()");

        try (BufferedReader in = new BufferedReader(
                                    new InputStreamReader(
                                        Thread.currentThread()
                                              .getContextClassLoader()
                                              .getResourceAsStream("posts/file-list.json")))) {

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } catch (IOException e) {
            return new Gson().toJson(headers.getRequestHeaders());
        }
    }
}
