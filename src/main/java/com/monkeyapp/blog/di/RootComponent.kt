package com.monkeyapp.blog.di

import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service
import javax.inject.Inject
import javax.servlet.ServletContext
import javax.ws.rs.core.Context

@Contract
interface RootComponent {
    fun sessionComponent(): SessionComponent
}

@Service
class RootComponentImpl : RootComponent {
    @Context
    private lateinit var context: ServletContext

    @Inject
    private lateinit var blogParameters: BlogParameters

    @Inject
    private lateinit var inputStreamProvider: InputStreamProvider

    private val objects = Objects()

    override fun sessionComponent(): SessionComponent = SessionComponentImpl(objects)

    private inner class Objects : SessionComponent.ParentComponent {
        override fun context(): ServletContext = context

        override fun blogParameters(): BlogParameters = blogParameters

        override fun inputStreamProvider(): InputStreamProvider = inputStreamProvider
    }
}
