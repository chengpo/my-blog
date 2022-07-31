package com.monkeyapp.blog.di

import com.monkeyapp.blog.controllers.FeedController
import com.monkeyapp.blog.controllers.PageController
import com.monkeyapp.blog.controllers.PostController
import com.monkeyapp.blog.models.*
import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service
import javax.inject.Inject

@Contract
interface RootComponent:
    PostController.ParentComponent,
    PageController.ParentComponent,
    FeedController.ParentComponent

@Service
class RootComponentImpl : RootComponent {
    @Inject
    private lateinit var blogParameters: BlogParameters

    @Inject
    private lateinit var contentComponent: ContentComponent

    override fun blogParameters(): BlogParameters = blogParameters

    override fun postStreamProvider(): BlogStreamProvider = contentComponent.blogStreamProvider(POST_ROOT)

    override fun pageStreamProvider(): BlogStreamProvider = contentComponent.blogStreamProvider(PAGE_ROOT)

    override fun completeContentProvider(): CompleteContentProvider = contentComponent.completeContentProvider()

    override fun partialContentProvider(): PartialContentProvider = contentComponent.partialContentProvider()

    companion object {
        const val POST_ROOT = "/md/posts"
        const val PAGE_ROOT = "/md/pages"
    }
}
