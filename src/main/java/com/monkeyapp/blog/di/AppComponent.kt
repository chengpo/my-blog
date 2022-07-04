package com.monkeyapp.blog.di

import com.monkeyapp.blog.controllers.PageController
import com.monkeyapp.blog.controllers.PostController
import com.monkeyapp.blog.models.*
import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service
import java.io.InputStream
import javax.inject.Inject
import javax.servlet.ServletContext
import javax.ws.rs.core.Context

@Contract
interface AppComponent:
    BlogStreamProvider.ParentComponent,
    PostController.ParentComponent,
    PageController.ParentComponent

@Service
class AppComponentImpl: AppComponent {
    @Context
    private lateinit var context: ServletContext

    @Inject
    private lateinit var inputStreamProvider: InputStreamProvider

    override fun postStreamProvider(): BlogStreamProvider {
        return BlogStreamProviderImpl(POST_ROOT, this)
    }

    override fun pageStreamProvider(): BlogStreamProvider {
        return BlogStreamProviderImpl(PAGE_ROOT, this)
    }

    override fun completeContentOf(): (String) -> String = CompleteContentProviderImpl(this).completeContentOf()

    override fun partialContentOf(): (String) -> String = PartialContentProviderImpl(this).partialContentOf()

    override fun context(): ServletContext = context

    override fun blogParameters(): BlogParameters = BlogParametersImpl(context)

    override fun inputStreamOf(): (String) -> InputStream = inputStreamProvider.inputStreamOf()

    companion object {
        const val POST_ROOT = "/md/posts"
        const val PAGE_ROOT = "/md/pages"
    }
}


