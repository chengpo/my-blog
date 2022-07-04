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
            .limit(postPerChunk)
            .filter { metadata ->
                tag.isEmpty() || tag == metadata.tag
            }
            .map { metadata ->
                PostDto(
                    metadata = PostMetadataDto(
                        crtime = metadata.crtime,
                        url = "posts/${metadata.year}/${metadata.monthday}/${metadata.title}",
                        title = metadata.capitalizedTitle,
                        tag = metadata.tag
                    ),
                    content = partialContentOf(metadata.path))
            }.collect(Collectors.toList())

        return PostChunkDto(
            posts = postDtos,
            offset = offset,
            hasMore = offset + postPerChunk < postStreamProvider.total()
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
                        url = "posts/${metadata.year}/${metadata.monthday}/${metadata.title}",
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

