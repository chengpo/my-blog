package com.monkeyapp.blog.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter @Getter
public class PaperFileDto {
    @JsonProperty("creationTime")
    @NonNull
    private String creationTime;

    @JsonProperty("url")
    @NonNull
    private String url;

    @JsonProperty("title")
    @NonNull
    private String title;

    @JsonProperty("tag")
    @NonNull
    private String tag;
}
