package com.monkeyapp.blog.controllers

import com.monkeyapp.blog.models.BlogStreamProvider
import com.monkeyapp.blog.models.CompleteContentProvider
import com.monkeyapp.blog.dtos.PageDto
import com.monkeyapp.blog.dtos.PageMetadataDto
import com.monkeyapp.blog.models.capitalizedTitle
import java.util.*

class PageController(component: ParentComponent) {
    private val pageStreamProvider = component.pageStreamProvider()
    private val completeContentOf = component.completeContentOf()

    fun pageContent(title: String): Optional<PageDto> {
        return pageStreamProvider.metaStream()
            .filter { metadata -> metadata.title == title }
            .findFirst()
            .map { metadata ->
                PageDto(
                    metadata = PageMetadataDto(
                        crtime = metadata.crtime,
                        url = "pages/${metadata.title}",
                        title = metadata.capitalizedTitle
                    ),
                    content =completeContentOf(metadata.path))
            }
    }

    interface ParentComponent : CompleteContentProvider {
        fun pageStreamProvider(): BlogStreamProvider
    }
}