package com.monkeyapp.blog.controllers

import com.monkeyapp.blog.di.BlogParameters
import com.monkeyapp.blog.dtos.PostChunkDto
import com.monkeyapp.blog.dtos.PostDto
import com.monkeyapp.blog.dtos.PostMetadataDto
import com.monkeyapp.blog.models.*
import java.util.*
import java.util.stream.Collectors
import kotlin.Comparator

class PostController(dependencies: Dependencies)  {
    private val postStreamProvider = dependencies.postStreamProvider()
    private val partialContentProvider = dependencies.partialContentProvider()
    private val completeContentProvider = dependencies.completeContentProvider()
    private val blogParameters = dependencies.blogParameters()

    fun postChunk(tag: String, offset: Long): PostChunkDto {
        return postStreamProvider.metaStream()
            .sorted(Comparator.comparingLong(BlogMetadata::priority).reversed())
            .skip(offset)
            .filter { metadata ->
                tag.isEmpty() || tag.compareTo(metadata.tag, ignoreCase = true) == 0
            }
            .limit(blogParameters.postPerChunk())
            .map(this::toPartialPostDto)
            .collect(Collectors.toList())
            .let { posts ->
                PostChunkDto(
                    posts = posts,
                    offset = offset,
                    capacity = blogParameters.postPerChunk()
                )
            }
    }

    fun postContent(year: String, monthday: String, title: String): Optional<PostDto> {
        return postStreamProvider.metaStream()
            .filter { metadata ->
                metadata.year == year &&
                metadata.monthday == monthday &&
                metadata.title == title
            }
            .findFirst()
            .map(this::toCompletePostDto)
    }

    private fun toPartialPostDto(metadata: BlogMetadata) = toPostDto(metadata, partialContentProvider::contentOf)

    private fun toCompletePostDto(metadata: BlogMetadata) = toPostDto(metadata, completeContentProvider::contentOf)

    private fun toPostDto(metadata: BlogMetadata, contentOf: (String) -> String): PostDto {
        return PostDto(
            metadata = PostMetadataDto(
                crtime = metadata.crtime,
                url = metadata.postUrl,
                title = metadata.capitalizedTitle,
                tag = metadata.capitalizedTag
                ),
            content = contentOf(metadata.path)
        )
    }

    interface Dependencies {
        fun blogParameters(): BlogParameters
        fun postStreamProvider(): BlogStreamProvider
        fun completeContentProvider(): ContentProvider
        fun partialContentProvider(): ContentProvider
    }
}
