package com.monkeyapp.blog.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class PageDto (
    @JsonProperty("metadata")
    val metadata: PageMetadataDto,

    @JsonProperty("content")
    var content: String
)

data class PageMetadataDto(
    @JsonProperty("creationTime")
    val crtime: String,

    @JsonProperty("url")
    val url: String,

    @JsonProperty("title")
    val title: String
)