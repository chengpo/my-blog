package com.monkeyapp.blog.controllers

import com.monkeyapp.blog.models.BlogStreamProvider
import com.monkeyapp.blog.models.CompleteContentProvider
import com.monkeyapp.blog.dtos.PageDto
import com.monkeyapp.blog.dtos.PageMetadataDto
import com.monkeyapp.blog.models.BlogMetadata
import com.monkeyapp.blog.models.capitalizedTitle
import java.util.*

class PageController(component: ParentComponent) {
    private val pageStreamProvider = component.pageStreamProvider()
    private val completeContentProvider = component.completeContentProvider()

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

    interface ParentComponent {
        fun pageStreamProvider(): BlogStreamProvider
        fun completeContentProvider(): CompleteContentProvider
    }
}
