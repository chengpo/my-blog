package com.monkeyapp.blog.controllers

import com.monkeyapp.blog.dtos.FeedChannelDto
import com.monkeyapp.blog.dtos.FeedItemDto
import com.monkeyapp.blog.dtos.SyncFeedDto
import com.monkeyapp.blog.models.*
import java.util.stream.Collectors

interface FeedController {
    fun feed(): SyncFeedDto

    interface ParentComponent {
        fun postStreamProvider(): BlogStreamProvider
        fun partialContentProvider(): PartialContentProvider
    }
}

class FeedControllerImpl(component: FeedController.ParentComponent) : FeedController {
    private val postStreamProvider = component.postStreamProvider()
    private val partialContentProvider = component.partialContentProvider()

    override fun feed(): SyncFeedDto {
        return postStreamProvider.metaStream()
            .sorted(Comparator.comparingLong(BlogMetadata::priority).reversed())
            .map(this::toFeedItemDto)
            .collect(Collectors.toList())
            .run(this::toFeedChannelDto)
            .run(this::toSyncFeedDto)
    }

    private fun toFeedItemDto(metadata: BlogMetadata): FeedItemDto {
        return FeedItemDto(
            title = metadata.capitalizedTitle,
            link = "http://monkey-blogger.herokuapp.com/${metadata.postUrl}",
            guid = "http://monkey-blogger.herokuapp.com/${metadata.postUrl}",
            description = "${partialContentProvider.contentOf(metadata.path)} <p> ... </p>",
            pubDate = metadata.pubDate
        )
    }

    private fun toFeedChannelDto(feedItems: List<FeedItemDto>): FeedChannelDto {
        return FeedChannelDto(
            title = "Monkey Blogger",
            link = "http://monkey-blogger.herokuapp.com",
            description = "Some random thought",
            items = feedItems
        )
    }

    private fun toSyncFeedDto(feedChannelDto: FeedChannelDto): SyncFeedDto {
        return SyncFeedDto(version = "2.0", channel = feedChannelDto)
    }
}
