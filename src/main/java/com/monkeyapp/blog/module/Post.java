package com.monkeyapp.blog.module;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Post implements Comparable<Post>{
    @JsonProperty("entity")
    private final Entity entity;

    @JsonProperty("content")
    private final String content;

    public Post(Entity entity, String content) {
        this.entity = entity;
        this.content = content;
    }

    public Entity getEntity() {
        return entity;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int compareTo(Post other) {
        return entity.compareTo(other.entity);
    }
}
