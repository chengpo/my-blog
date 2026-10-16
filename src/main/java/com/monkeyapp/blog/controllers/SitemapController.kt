package com.monkeyapp.blog.controllers

import com.monkeyapp.blog.dtos.SiteUrlDto
import com.monkeyapp.blog.dtos.SitemapDto
import com.monkeyapp.blog.models.BlogMetadata
import com.monkeyapp.blog.models.BlogStreamProvider
import com.monkeyapp.blog.models.postUrl
import com.monkeyapp.blog.models.priority
import java.util.stream.Collectors

class SitemapController(dependencies: Dependencies) {
    private val postStreamProvider = dependencies.postStreamProvider()
    private val pageStreamProvider = dependencies.pageStreamProvider()

    fun sitemap(): SitemapDto {
        return listOf(postStreamProvider, pageStreamProvider)
            .map(BlogStreamProvider::metaStream)
            .flatMap { 
                it.sorted(Comparator.comparingLong(BlogMetadata::priority).reversed())
                .collect(Collectors.toList()) }
            .map(this::toSiteUrlDto)
            .let { SitemapDto(url = it) }
    }

    private fun toSiteUrlDto(metadata: BlogMetadata): SiteUrlDto {
        return SiteUrlDto(
            loc = "https://chengpo.github.io/${metadata.postUrl}",
        )
    }
     
    interface Dependencies {
        fun postStreamProvider(): BlogStreamProvider
        fun pageStreamProvider(): BlogStreamProvider
    }
}