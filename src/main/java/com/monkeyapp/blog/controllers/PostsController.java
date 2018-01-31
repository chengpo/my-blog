/*
MIT License

Copyright (c) 2017 - 2018 Po Cheng

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

import com.monkeyapp.blog.dtos.PaperChunkDto;
import com.monkeyapp.blog.dtos.PaperDto;
import com.monkeyapp.blog.dtos.TypeConverter;
import com.monkeyapp.blog.models.*;
import com.monkeyapp.blog.models.PaperChunk;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/posts")
public class PostsController {
    @Context
    private ServletContext servletContext;

    @Inject
    private PaperRepository paperRepository;

    @Inject
    private TypeConverter typeConverter;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PaperChunkDto getPostChunk(@DefaultValue("") @QueryParam("tag") String tag,
                                      @DefaultValue("0") @QueryParam("offset") int offset) {
        final int chunkCapacity = Integer.valueOf(servletContext.getInitParameter("post-per-chunk"));
        final PaperChunk paperChunk = paperRepository.getPostPaperChunk(tag, offset, chunkCapacity);
        return typeConverter.toPaperChunkDto(paperChunk);
    }

    @GET
    @Path("/{year}/{monthday}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public PaperDto getPostContent(@PathParam("year") String year,
                                   @PathParam("monthday") String monthDay,
                                   @PathParam("title") String title) {
        return paperRepository.getCompletePost(year, monthDay, title)
                              .map(typeConverter::toPaperDto)
                              .orElseThrow(() -> new WebApplicationException(404));
    }
}
