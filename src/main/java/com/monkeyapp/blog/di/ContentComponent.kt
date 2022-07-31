package com.monkeyapp.blog.di

import com.monkeyapp.blog.models.BlogStreamProvider
import com.monkeyapp.blog.models.CompleteContentProvider
import com.monkeyapp.blog.models.PartialContentProvider
import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service
import javax.inject.Inject
import javax.servlet.ServletContext
import javax.ws.rs.core.Context

@Contract
interface ContentComponent {
    fun blogStreamProvider(root: String): BlogStreamProvider
    fun completeContentProvider(): CompleteContentProvider
    fun partialContentProvider(): PartialContentProvider
}

@Service
class ContentComponentImpl : ContentComponent {
    @Context
    private lateinit var context: ServletContext

    @Inject
    private lateinit var blogParameters: BlogParameters

    @Inject
    private lateinit var inputStreamProvider: InputStreamProvider

    override fun blogStreamProvider(root: String): BlogStreamProvider {
        return BlogStreamProvider(root, context, inputStreamProvider)
    }

    override fun completeContentProvider(): CompleteContentProvider {
        return CompleteContentProvider(inputStreamProvider)
    }

    override fun partialContentProvider(): PartialContentProvider {
        return PartialContentProvider(inputStreamProvider, blogParameters)
    }
}