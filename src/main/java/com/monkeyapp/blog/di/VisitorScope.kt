package com.monkeyapp.blog.di

import com.monkeyapp.blog.controllers.*
import com.monkeyapp.blog.models.*
import javax.servlet.ServletContext

interface VisitorScope {
    fun feedController(): FeedController
    fun pageController(): PageController
    fun postController(): PostController

    interface ParentComponent {
        fun context(): ServletContext
        fun inputStreamProvider(): InputStreamProvider
        fun blogParameters(): BlogParameters
    }
}

class VisitorScopeImpl(private val parentComponent: VisitorScope.ParentComponent) : VisitorScope {
    private val visitorComponent = Component()

    override fun feedController(): FeedController = FeedController(visitorComponent)

    override fun pageController(): PageController = PageController(visitorComponent)

    override fun postController(): PostController = PostController(visitorComponent)

    private inner class Component :
        PostController.ParentComponent,
        PageController.ParentComponent,
        FeedController.ParentComponent {
        private val blogStreamProviderFactory = BlogStreamProviderFactory(
            parentComponent.context(),
            parentComponent.inputStreamProvider())

        private val contentProviderFactory = ContentProviderFactory(
            parentComponent.inputStreamProvider(),
            parentComponent.blogParameters())

        override fun blogParameters(): BlogParameters {
            return parentComponent.blogParameters()
        }

        override fun pageStreamProvider(): BlogStreamProvider {
            return blogStreamProviderFactory.pageStreamProvider()
        }

        override fun postStreamProvider(): BlogStreamProvider {
            return blogStreamProviderFactory.postStreamProvider()
        }

        override fun completeContentProvider(): ContentProvider {
            return contentProviderFactory.completeContentProvider()
        }

        override fun partialContentProvider(): ContentProvider {
            return contentProviderFactory.partialContentProvider()
        }
    }
}
