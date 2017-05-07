package com.monkeyapp.blog.rest.module;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Paper implements Comparable<Paper>{
    @JsonProperty("entity")
    private final Entity entity;

    @JsonProperty("content")
    private final String content;

    public Paper(Entity entity, String content) {
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
    public int compareTo(Paper other) {
        return entity.compareTo(other.entity);
    }
}
