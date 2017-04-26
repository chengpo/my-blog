package com.monkeyapp.blog.rest.module;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostEntity {
    @JsonProperty("post")
    private PostHead post;

    @JsonProperty("content")
    private String content;

    public PostEntity(PostHead post, String content) {
        this.post = post;
        this.content = content;
    }
}
