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

package com.monkeyapp.blog.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monkeyapp.blog.module.*;
import com.monkeyapp.blog.reader.MarkdownReader;
import com.monkeyapp.blog.reader.TextReader;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/posts")
public class PostsResource {
    private static final int NUM_OF_POST_PER_PAGE = 3;

    private static class PostChunk {
        @JsonProperty("posts")
        private final List<Post> posts;

        @JsonProperty("offset")
        private final int offset; // offset to the very first blog

        @JsonProperty("eof")
        private final boolean eof; // eof = true when reach the last blog

        private PostChunk(List<Post> posts, int offset, boolean eof) {
            this.posts = posts;
            this.offset = offset;
            this.eof = eof;
        }
    }

    @Context
    private ServletContext context;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PostChunk getPostChunk(@DefaultValue("") @QueryParam("tag") String tag,
                                  @DefaultValue("0") @QueryParam("offset") int offset) throws FileNotFoundException {

        final List<Entity> entities = new PostRepository(context.getRealPath("/")).getPostEntities(tag);
        final List<Post> posts = entities.stream()
                        .skip(offset)
                        .limit(NUM_OF_POST_PER_PAGE)
                        .map((entity) -> {
                                final String path = context.getRealPath("/") + ("md/posts/" + entity.getName()).replace("/", File.separator);
                                final String content = new MarkdownReader(
                                                            TextReader.partialReader(path)).read();
                                return new Post(entity, content);
                            }
                        )
                        .collect(Collectors.toList());

        final boolean eof = offset + posts.size() >= entities.size();
        return new PostChunk(posts, offset, eof);
    }

    @GET @Path("/{year}/{monthday}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post getPostDetail(@PathParam("year") String year, @PathParam("monthday") String monthDay,
                              @PathParam("title") String title) throws FileNotFoundException {
        return Optional.ofNullable(new PostRepository(context.getRealPath("/")).getPostEntity(year, monthDay, title))
                       .map((entity)-> {
                                    final String path = context.getRealPath("/") + ("md/posts/" + entity.getName()).replace("/", File.separator);
                                    final String content = new MarkdownReader(
                                                                TextReader.fullReader(path)).read();
                                    return new Post(entity, content);
                               }
                       )
                       .orElseThrow(() -> new WebApplicationException(404));
    }
}
