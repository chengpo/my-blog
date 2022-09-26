package com.monkeyapp.blog.controllers

import com.monkeyapp.blog.models.BlogStreamProvider
import com.monkeyapp.blog.models.ContentProvider
import com.monkeyapp.blog.dtos.PageDto
import com.monkeyapp.blog.dtos.PageMetadataDto
import com.monkeyapp.blog.models.BlogMetadata
import com.monkeyapp.blog.models.capitalizedTitle
import java.util.*

class PageController(dependencies: Dependencies)  {
    private val pageStreamProvider = dependencies.pageStreamProvider()
    private val completeContentProvider = dependencies.completeContentProvider()

    fun pageContent(title: String): Optional<PageDto> {
        return pageStreamProvider.metaStream()
            .filter { metadata -> metadata.title == title }
            .findFirst()
            .map(this::toPageDto)
    }

    private fun toPageDto(metadata: BlogMetadata): PageDto {
        return PageDto(
            metadata = PageMetadataDto(
                crtime = metadata.crtime,
                url = "pages/${metadata.title}",
                title = metadata.capitalizedTitle
            ),
            content = completeContentProvider.contentOf(metadata.path)
        )
    }

    interface Dependencies {
        fun pageStreamProvider(): BlogStreamProvider
        fun completeContentProvider(): ContentProvider
    }
}
