package com.monkeyapp.blog.rest.module;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PostEntity implements Comparable<PostEntity>{
    @JsonProperty("head")
    private PostHead head;

    @JsonProperty("content")
    private String content;

    PostEntity(PostHead head, String content) {
        this.head = head;
        this.content = content;
    }

    @Override
    public int compareTo(PostEntity other) {
        return head.compareTo(other.head);
    }
}
