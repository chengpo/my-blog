package com.monkeyapp.blog.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TagViewModel implements Comparable<TagViewModel> {
    @JsonProperty("tag")
    private final String name;

    @JsonProperty("count")
    private final long count;

    public TagViewModel(String name, long count) {
        this.name = name;
        this.count = count;
    }

    @Override
    public int compareTo(TagViewModel other) {
        return name.compareTo(other.name);
    }
}
