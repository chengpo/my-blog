package com.monkeyapp.blog.di

import com.monkeyapp.blog.controllers.*
import com.monkeyapp.blog.models.*
import javax.servlet.ServletContext

interface SessionComponent {
    fun feedController(): FeedController
    fun pageController(): PageController
    fun postController(): PostController

    interface ParentComponent {
        fun context(): ServletContext
        fun inputStreamProvider(): InputStreamProvider
        fun blogParameters(): BlogParameters
    }
}

class SessionComponentImpl(private val parentComponent: SessionComponent.ParentComponent) : SessionComponent {
    private val objects = Objects()

    override fun feedController(): FeedController = FeedController(objects)

    override fun pageController(): PageController = PageController(objects)

    override fun postController(): PostController = PostController(objects)

    private inner class Objects :
        PostController.ParentComponent,
        PageController.ParentComponent,
        FeedController.ParentComponent {

        override fun blogParameters(): BlogParameters {
            return parentComponent.blogParameters()
        }

        override fun pageStreamProvider(): BlogStreamProvider {
            return blogStreamProvider(PAGE_ROOT)
        }

        override fun postStreamProvider(): BlogStreamProvider {
            return blogStreamProvider(POST_ROOT)
        }

        override fun completeContentProvider(): ContentProvider {
            return ContentProvider(
                parentComponent.inputStreamProvider(),
                completeContentReader())
        }

        override fun partialContentProvider(): ContentProvider {
            return ContentProvider(
                parentComponent.inputStreamProvider(),
                partialContentReader())
        }

        private fun completeContentReader(): ContentReader {
            return CompleteContentReader()
        }

        private fun partialContentReader(): ContentReader {
            return PartialContentReader(parentComponent.blogParameters())
        }

        private fun blogStreamProvider(root: String): BlogStreamProvider {
            return BlogStreamProvider(
                root,
                parentComponent.context(),
                parentComponent.inputStreamProvider()
            )
        }
    }

    companion object {
        const val POST_ROOT = "/md/posts"
        const val PAGE_ROOT = "/md/pages"
    }
}
