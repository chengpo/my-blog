package com.monkeyapp.blog.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import java.util.List;

@Setter @Getter
public class PaperChunkDto {
    @JsonProperty("papers")
    @NonNull
    private List<PaperDto> papers;

    @JsonProperty("offset")
    private int offset; // offset to the very first blog

    @JsonProperty("capacity")
    private int capacity;

    @JsonProperty("eof")
    private boolean eof; // eof = true when reach the last blog
}
