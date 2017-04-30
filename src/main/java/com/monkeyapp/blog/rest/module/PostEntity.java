package com.monkeyapp.blog.rest.module;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostEntity implements Comparable<PostEntity>{
    @JsonProperty("header")
    private PostHead header;

    @JsonProperty("content")
    private String content;

    PostEntity(PostHead header, String content) {
        this.header = header;
        this.content = content;
    }

    @Override
    public int compareTo(PostEntity other) {
        return header.compareTo(other.header);
    }
}
