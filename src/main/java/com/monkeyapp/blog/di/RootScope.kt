package com.monkeyapp.blog.di

import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service
import javax.inject.Inject
import javax.servlet.ServletContext
import javax.ws.rs.core.Context

@Contract
interface RootScope {
    fun visitorScope(): VisitorScope
}

@Service
class RootScopeImpl : RootScope {
    @Context
    private lateinit var context: ServletContext

    @Inject
    private lateinit var blogParameters: BlogParameters

    @Inject
    private lateinit var inputStreamProvider: InputStreamProvider

    private val rootComponent = Component()

    override fun visitorScope(): VisitorScope =  VisitorScopeImpl(rootComponent)

    private inner class Component : VisitorScope.ParentComponent {
        override fun context(): ServletContext = context

        override fun blogParameters(): BlogParameters = blogParameters

        override fun inputStreamProvider(): InputStreamProvider = inputStreamProvider
    }
}
