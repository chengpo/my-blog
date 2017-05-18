package com.monkeyapp.blog.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monkeyapp.blog.rest.module.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
        private final int offset;

        @JsonProperty("eof")
        private final boolean eof;

        private PostChunk(List<Post> posts, int offset, boolean eof) {
            this.posts = posts;
            this.offset = offset;
            this.eof = eof;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PostChunk getPostChunk(@DefaultValue("") @QueryParam("tag") String tag,
                                  @DefaultValue("0") @QueryParam("offset") int offset) {
        final List<Entity> entities = new PostBank().getPostEntities(tag);
        final List<Post> posts = entities.stream()
                        .skip(offset)
                        .limit(NUM_OF_POST_PER_PAGE)
                        .map((entity) -> {
                                final String path = "posts/" + entity.getName();
                                final String content = new MarkdownReader(
                                                            TextReader.partialReader(path)).read();
                                return new Post(entity, content);
                            }
                        )
                        .collect(Collectors.toList());

        final boolean eof = offset + posts.size() >= entities.size();
        return new PostChunk(posts, offset, eof);
    }

    @GET @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post getPostDetail(@PathParam("name") String name) {
        return Optional.ofNullable(Entity.fromFileName(name))
                       .flatMap((entity)-> {
                                    final String path = "posts/" + entity.getName();
                                    final String content = new MarkdownReader(
                                                                TextReader.fullReader(path)).read();
                                    return Optional.of(new Post(entity, content));
                               }
                       )
                       .orElseThrow(() -> new WebApplicationException(404));
    }
}
