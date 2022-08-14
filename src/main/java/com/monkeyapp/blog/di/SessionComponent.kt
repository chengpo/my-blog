package com.monkeyapp.blog.di

import com.monkeyapp.blog.controllers.FeedController
import com.monkeyapp.blog.controllers.PageController
import com.monkeyapp.blog.controllers.PostController
import com.monkeyapp.blog.models.BlogStreamProvider
import com.monkeyapp.blog.models.CompleteContentProvider
import com.monkeyapp.blog.models.PartialContentProvider
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

class SessionComponentImpl(private val parentComponent: SessionComponent.ParentComponent) :
    SessionComponent,
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

    private fun blogStreamProvider(root: String): BlogStreamProvider {
        return BlogStreamProvider(
            root,
            parentComponent.context(),
            parentComponent.inputStreamProvider()
        )
    }

    override fun completeContentProvider(): CompleteContentProvider {
        return CompleteContentProvider(parentComponent.inputStreamProvider())
    }

    override fun partialContentProvider(): PartialContentProvider {
        return PartialContentProvider(
            parentComponent.inputStreamProvider(),
            parentComponent.blogParameters()
        )
    }

    override fun feedController(): FeedController = FeedController(this)

    override fun pageController(): PageController = PageController(this)

    override fun postController(): PostController = PostController(this)

    companion object {
        const val POST_ROOT = "/md/posts"
        const val PAGE_ROOT = "/md/pages"
    }
}
