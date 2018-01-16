package com.monkeyapp.blog.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter @Getter
public class PaperDto {
    @JsonProperty("file")
    @NonNull
    private PaperFileDto file;

    @JsonProperty("content")
    @NonNull
    private String content;
}
