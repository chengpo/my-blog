package com.monkeyapp.blog.controllers

import com.monkeyapp.blog.dtos.FeedChannelDto
import com.monkeyapp.blog.dtos.FeedItemDto
import com.monkeyapp.blog.dtos.SyncFeedDto
import com.monkeyapp.blog.models.*
import java.util.stream.Collectors

class FeedController(component: ParentComponent) {
    private val postStreamProvider = component.postStreamProvider()
    private val partialContentProvider = component.partialContentProvider()

    fun feed(): SyncFeedDto {
        return postStreamProvider.metaStream()
            .sorted(Comparator.comparingLong(BlogMetadata::priority).reversed())
            .map { metadata ->
                FeedItemDto(
                    title = metadata.capitalizedTitle,
                    link = "http://monkey-blogger.herokuapp.com/${metadata.postUrl}",
                    guid = "http://monkey-blogger.herokuapp.com/${metadata.postUrl}",
                    description = "${partialContentProvider.contentOf(metadata.path)} <p> ... </p>",
                    pubDate = metadata.pubDate
                )
            }
            .collect(Collectors.toList())
            .run {
                FeedChannelDto(
                    title = "Monkey Blogger",
                    link = "http://monkey-blogger.herokuapp.com",
                    description = "Some random thought",
                    items = this
                )
            }
            .run {
                SyncFeedDto(version = "2.0", channel = this)
            }
    }

    interface ParentComponent {
        fun postStreamProvider(): BlogStreamProvider
        fun partialContentProvider(): PartialContentProvider
    }
}
