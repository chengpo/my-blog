package com.monkeyapp.blog.di

import com.monkeyapp.blog.controllers.FeedController
import com.monkeyapp.blog.controllers.PageController
import com.monkeyapp.blog.controllers.PostController
import com.monkeyapp.blog.models.BlogStreamProvider
import com.monkeyapp.blog.models.CompleteContentProvider
import com.monkeyapp.blog.models.PartialContentProvider
import javax.servlet.ServletContext

interface ControllerComponent {
    fun feedController(): FeedController
    fun pageController(): PageController
    fun postController(): PostController

    interface ParentComponent {
        fun context(): ServletContext
        fun inputStreamProvider(): InputStreamProvider
        fun blogParameters(): BlogParameters
    }
}

class ControllerComponentImpl(private val parentComponent: ControllerComponent.ParentComponent) :
    ControllerComponent,
    PostController.ParentComponent,
    PageController.ParentComponent,
    FeedController.ParentComponent {

    private val feedController: FeedController by lazy {
        FeedController(this)
    }

    private val pageController: PageController by lazy {
        PageController(this)
    }

    private val postController: PostController by lazy {
        PostController(this)
    }

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

    override fun feedController(): FeedController = feedController

    override fun pageController(): PageController = pageController

    override fun postController(): PostController = postController

    companion object {
        const val POST_ROOT = "/md/posts"
        const val PAGE_ROOT = "/md/pages"
    }
}
