package com.monkeyapp.blog.rest.module;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostEntity implements Comparable<PostEntity>{
    @JsonProperty("post")
    private PostHead post;

    @JsonProperty("content")
    private String content;

    PostEntity(PostHead post, String content) {
        this.post = post;
        this.content = content;
    }

    @Override
    public int compareTo(PostEntity other) {
        return post.compareTo(other.post);
    }
}
