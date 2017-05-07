package com.monkeyapp.blog.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.monkeyapp.blog.rest.module.PostBank;
import com.monkeyapp.blog.rest.module.MarkdownReader;
import org.glassfish.jersey.server.monitoring.MonitoringStatistics;

import javax.inject.Inject;
import javax.inject.Provider;
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
    @Inject
    Provider<MonitoringStatistics> monitoringStatisticsProvider;

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

        sb.append("monitoring = ");
        String monitoringJson = mapper.writeValueAsString(monitoringStatisticsProvider.get().getRequestStatistics());
        sb.append(monitoringJson);
        sb.append("\n\n");

        sb.append("posts = ");
        String postJson = mapper.writeValueAsString(
                            new PostBank().getPosts().stream()
                                                     .sorted(Comparator.reverseOrder())
                                                     .collect(Collectors.toList()));
        sb.append(postJson);

        return sb.toString();
    }
}
