package com.monkeyapp.blog.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TagCounter implements Comparable<TagCounter> {
    @JsonProperty("tag")
    private final String tag;

    @JsonProperty("count")
    private final long count;

    public TagCounter(String tag, long count) {
        this.tag = tag;
        this.count = count;
    }

    @Override
    public int compareTo(TagCounter other) {
        return tag.compareTo(other.tag);
    }
}
