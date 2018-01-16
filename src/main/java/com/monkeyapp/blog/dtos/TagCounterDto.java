package com.monkeyapp.blog.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter @Getter
public class TagCounterDto {
    @JsonProperty("tag")
    @NonNull
    private String tag;

    @JsonProperty("count")
    private long count;
}
