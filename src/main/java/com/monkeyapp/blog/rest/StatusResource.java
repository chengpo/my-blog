package com.monkeyapp.blog.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.monkeyapp.blog.rest.module.PostBank;
import com.monkeyapp.blog.rest.module.PostReader;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;
import java.util.stream.Collectors;

@Path("/status")
public class StatusResource {
    @GET
    @Produces({MediaType.TEXT_PLAIN})
    public String getStatus(@Context HttpHeaders headers) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        StringBuilder sb = new StringBuilder();

        sb.append("headers = ");
        String headerJson = mapper.writeValueAsString(headers.getRequestHeaders());
        sb.append(headerJson);
        sb.append("\n\n");

        sb.append("posts = ");
        String postJson = mapper.writeValueAsString(
                            new PostBank().getPosts().stream()
                                                     .sorted(Comparator.reverseOrder())
                                                     .map(PostReader::toBriefEntity)
                                                     .collect(Collectors.toList()));
        sb.append(postJson);

        return sb.toString();
    }
}
