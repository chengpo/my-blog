package com.monkeyapp.blog.controllers

import com.monkeyapp.blog.di.BlogParameters
import com.monkeyapp.blog.dtos.PostChunkDto
import com.monkeyapp.blog.dtos.PostDto
import com.monkeyapp.blog.dtos.PostMetadataDto
import com.monkeyapp.blog.models.*
import java.util.*
import java.util.stream.Collectors
import kotlin.Comparator

class PostController(component: ParentComponent) {
    private val postStreamProvider = component.postStreamProvider()
    private val partialContentProvider = component.partialContentProvider()
    private val completeContentProvider = component.completeContentProvider()
    private val blogParameters = component.blogParameters()

    fun postChunk(tag: String, offset: Long): PostChunkDto {
        return postStreamProvider.metaStream()
            .sorted(Comparator.comparingLong(BlogMetadata::priority).reversed())
            .skip(offset)
            .filter { metadata ->
                tag.isEmpty() || tag.compareTo(metadata.tag, ignoreCase = true) == 0
            }
            .limit(blogParameters.postPerChunk())
            .map { metadata ->
                PostDto(
                    metadata = metadata.toPostMetadataDto(),
                    content = metadata.partialContent())
            }
            .collect(Collectors.toList())
            .run {
                PostChunkDto(
                    posts = this,
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
            .map { metadata ->
                PostDto(
                    metadata = metadata.toPostMetadataDto(),
                    content = metadata.fullContent())
            }
    }
    
    private fun BlogMetadata.toPostMetadataDto(): PostMetadataDto {
        return PostMetadataDto(
                    crtime = this.crtime,
                    url = this.postUrl,
                    title = this.capitalizedTitle,
                    tag = this.capitalizedTag
               )
    }
    
    private fun BlogMetadata.partialContent(): String {
        return partialContentProvider.contentOf(path)
    }
    
    private fun BlogMetadata.fullContent(): String {
        return completeContentProvider.contentOf(path)
    }

    interface ParentComponent {
        fun blogParameters(): BlogParameters
        fun postStreamProvider(): BlogStreamProvider
        fun completeContentProvider(): CompleteContentProvider
        fun partialContentProvider(): PartialContentProvider
    }
}

