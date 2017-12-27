package com.monkeyapp.blog.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TagViewModel implements Comparable<TagViewModel> {
    @JsonProperty("tag")
    private final String tag;

    @JsonProperty("count")
    private final long count;

    public TagViewModel(String tag, long count) {
        this.tag = tag;
        this.count = count;
    }

    @Override
    public int compareTo(TagViewModel other) {
        return tag.compareTo(other.tag);
    }
}
