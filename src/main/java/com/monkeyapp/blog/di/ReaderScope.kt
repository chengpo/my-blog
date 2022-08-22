package com.monkeyapp.blog.di

import com.monkeyapp.blog.controllers.*
import com.monkeyapp.blog.models.*
import javax.servlet.ServletContext

interface ReaderScope {
    fun feedController(): FeedController
    fun pageController(): PageController
    fun postController(): PostController

    interface ParentComponent {
        fun context(): ServletContext
        fun inputStreamProvider(): InputStreamProvider
        fun blogParameters(): BlogParameters
    }
}

class ReaderScopeImpl(private val parentComponent: ReaderScope.ParentComponent) : ReaderScope {
    private val objects = Objects()

    override fun feedController(): FeedController = FeedController(objects)

    override fun pageController(): PageController = PageController(objects)

    override fun postController(): PostController = PostController(objects)

    private inner class Objects :
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
