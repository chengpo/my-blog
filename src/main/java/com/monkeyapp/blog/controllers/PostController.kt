package com.monkeyapp.blog.controllers

import com.monkeyapp.blog.dtos.PostChunkDto
import com.monkeyapp.blog.dtos.PostDto
import com.monkeyapp.blog.dtos.PostMetadataDto
import com.monkeyapp.blog.models.*
import java.util.*
import java.util.stream.Collectors

class PostController(component: ParentComponent) {
    private val postStreamProvider = component.postStreamProvider()
    private val partialContentOf = component.partialContentOf()
    private val completeContentOf = component.completeContentOf()
    private val postPerChunk  = component.blogParameters().postPerChunk()

    fun postChunk(tag: String, offset: Long): PostChunkDto {
        val postDtos = postStreamProvider.metaStream()
            .sorted(Comparator.comparingLong(BlogMetadata::priority).reversed())
            .skip(offset)
            .filter { metadata ->
                tag.isEmpty() || tag.compareTo(metadata.tag, ignoreCase = true) == 0
            }
            .limit(postPerChunk)
            .map { metadata ->
                PostDto(
                    metadata = PostMetadataDto(
                        crtime = metadata.crtime,
                        url = metadata.postUrl,
                        title = metadata.capitalizedTitle,
                        tag = metadata.capitalizedTag
                    ),
                    content = partialContentOf(metadata.path))
            }.collect(Collectors.toList())

        return PostChunkDto(
            posts = postDtos,
            offset = offset,
            capacity = postPerChunk
        )
    }

    fun postContent(year: String, monthday: String, title: String): Optional<PostDto> {
        return postStreamProvider.metaStream()
            .filter { metadata ->
                    metadata.year == year &&
                    metadata.monthday == monthday &&
                    metadata.title == title
            }
            .findFirst()
            .map { metadata ->
                PostDto(
                    metadata = PostMetadataDto(
                        crtime = metadata.crtime,
                        url = metadata.postUrl,
                        title = metadata.capitalizedTitle,
                        tag = metadata.tag
                    ),
                    content = completeContentOf(metadata.path))
            }
    }

    interface ParentComponent : CompleteContentProvider, PartialContentProvider {
        fun blogParameters(): BlogParameters
        fun postStreamProvider(): BlogStreamProvider
    }
}

