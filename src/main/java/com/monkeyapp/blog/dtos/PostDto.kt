/*
MIT License

Copyright (c) 2017 - 2022 Po Cheng

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.monkeyapp.blog.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class PostChunkDto(
    @JsonProperty("posts") 
    val posts: List<PostDto>,

    @JsonProperty("offset") 
    val offset: Long,

    @JsonProperty("capacity")
    val capacity: Long
)

data class PostDto (
    @JsonProperty("metadata")
    val metadata: PostMetadataDto,

    @JsonProperty("content")
    val content: String
)

data class PostMetadataDto(
    @JsonProperty("creationTime")
    val crtime: String,

    @JsonProperty("url")
    val url: String,

    @JsonProperty("title")
    val title: String,

    @JsonProperty("tag")
    val tag: String
)