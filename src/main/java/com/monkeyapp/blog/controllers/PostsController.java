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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monkeyapp.blog.models.*;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/posts")
public class PostsController {
    @Context
    ServletContext servletContext;

    @Inject
    PostRepository postRepository;

    @Inject
    PaperAdapter paperAdapter;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PostChunk getPostChunk(@DefaultValue("") @QueryParam("tag") String tag,
                                  @DefaultValue("0") @QueryParam("offset") int offset) {
        final int post_per_chunk = Integer.valueOf(servletContext.getInitParameter("post-per-chunk"));

        final long totalPosts = postRepository.getPostIds(tag).count();
        final List<Paper> posts = postRepository.getPostIds(tag)
                                             .sorted(Comparator.reverseOrder())
                                             .skip(offset)
                                             .limit(post_per_chunk)
                                             .map(paperAdapter::toPartialPost)
                                             .filter(Optional::isPresent)
                                             .map(Optional::get)
                                             .collect(Collectors.toList());

        final boolean eof = offset + posts.size() >= totalPosts;
        return new PostChunk(posts, offset, eof);
    }

    @GET
    @Path("/{year}/{monthday}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Paper getPostContent(@PathParam("year") String year, @PathParam("monthday") String monthDay,
                              @PathParam("title") String title) {
        return postRepository.getPostIds(year, monthDay, title)
                             .findFirst()
                             .map(paperAdapter::toCompletePost)
                             .filter(Optional::isPresent)
                             .map(Optional::get)
                             .orElseThrow(() -> new WebApplicationException(404));
    }

    private static class PostChunk {
        @JsonProperty("posts")
        private final List<Paper> posts;

        @JsonProperty("offset")
        private final int offset; // offset to the very first blog

        @JsonProperty("eof")
        private final boolean eof; // eof = true when reach the last blog

        private PostChunk(List<Paper> posts, int offset, boolean eof) {
            this.posts = posts;
            this.offset = offset;
            this.eof = eof;
        }
    }
}
