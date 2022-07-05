package com.monkeyapp.blog.controllers

import com.monkeyapp.blog.dtos.FeedChannelDto
import com.monkeyapp.blog.dtos.FeedItemDto
import com.monkeyapp.blog.dtos.SyncFeedDto
import com.monkeyapp.blog.models.*
import java.util.stream.Collectors

class FeedController(componet: ParentComponent) {
    private val postStreamProvider = componet.postStreamProvider()
    private val partialContentOf = componet.partialContentOf()
    fun feed(): SyncFeedDto {
        val feedItems = postStreamProvider.metaStream()
            .sorted(Comparator.comparingLong(BlogMetadata::priority).reversed())
            .map { metadata ->
                FeedItemDto(
                    title = metadata.capitalizedTitle,
                    link = "http://monkey-blogger.herokuapp.com/${metadata.postUrl}",
                    guid = "http://monkey-blogger.herokuapp.com/${metadata.postUrl}",
                    description = "${partialContentOf(metadata.path)} <p> ... </p>",
                    pubDate = ""//metadata.pubDate
                )
            }
            .collect(Collectors.toList())

        val feedChannel = FeedChannelDto(
            title = "Monkey Blogger",
            link = "http://monkey-blogger.herokuapp.com",
            description = "A simple tech blog",
            items = feedItems
        )

        return SyncFeedDto(version = "2.0", channel = feedChannel)
    }

    interface ParentComponent : PartialContentProvider {
        fun postStreamProvider(): BlogStreamProvider
    }
}