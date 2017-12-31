package com.monkeyapp.blog.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TagCounter {
    @JsonProperty("tag")
    private final String tag;

    @JsonProperty("count")
    private final long count;

    TagCounter(String tag, long count) {
        this.tag = tag;
        this.count = count;
    }

    String getTag() {
        return tag;
    }

    long getCount() {
        return count;
    }
}
