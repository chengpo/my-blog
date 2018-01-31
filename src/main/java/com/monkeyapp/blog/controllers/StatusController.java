/*
MIT License

Copyright (c) 2017 Po Cheng

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.monkeyapp.blog.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.monkeyapp.blog.models.Paper;
import com.monkeyapp.blog.models.PaperFile;
import com.monkeyapp.blog.models.PaperRepository;
import org.glassfish.jersey.server.monitoring.MonitoringStatistics;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/status")
public class StatusController {
    @Inject
    private Provider<MonitoringStatistics> monitoringStatisticsProvider;

    @Inject
    private PaperRepository paperRepository;

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

        final MonitoringStatistics statistics = monitoringStatisticsProvider.get();
        if (statistics != null) {
            sb.append("monitoring = ");
            String monitoringJson = mapper.writeValueAsString(statistics.getRequestStatistics());
            sb.append(monitoringJson);
            sb.append("\n\n");
        }

        sb.append("posts = ");
        List<String> postNames = paperRepository.getPostPaperChunk("", 0, Integer.MAX_VALUE)
                                         .getPapers()
                                         .stream()
                                         .map(Paper::getFile)
                                         .map(PaperFile::getTitle)
                                         .collect(Collectors.toList());

        sb.append(mapper.writeValueAsString(postNames));

        return sb.toString();
    }
}
