package com.monkeyapp.blog.controllers

import com.monkeyapp.blog.di.BlogParameters
import com.monkeyapp.blog.dtos.FeedChannelDto
import com.monkeyapp.blog.dtos.FeedItemDto
import com.monkeyapp.blog.dtos.SyncFeedDto
import com.monkeyapp.blog.models.*
import java.util.stream.Collectors

class FeedController(dependencies: Dependencies)  {
    private val postStreamProvider = dependencies.postStreamProvider()
    private val partialContentProvider = dependencies.partialContentProvider()
    private val blogParameters = dependencies.blogParameters()

    fun feed(): SyncFeedDto {
        return postStreamProvider.metaStream()
            .sorted(Comparator.comparingLong(BlogMetadata::priority).reversed())
            .map(this::toFeedItemDto)
            .collect(Collectors.toList())
            .let(this::toFeedChannelDto)
            .let(this::toSyncFeedDto)
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
            description = blogParameters.siteTitle(),
            items = feedItems
        )
    }

    private fun toSyncFeedDto(feedChannelDto: FeedChannelDto): SyncFeedDto {
        return SyncFeedDto(version = "2.0", channel = feedChannelDto)
    }

    interface Dependencies {
        fun postStreamProvider(): BlogStreamProvider
        fun partialContentProvider(): ContentProvider
        fun blogParameters(): BlogParameters
    }
}
