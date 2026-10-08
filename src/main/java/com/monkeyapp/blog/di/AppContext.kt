package com.monkeyapp.blog.di

import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service
import javax.servlet.ServletContext
import javax.ws.rs.core.Context

@Contract
interface AppContext {
    fun getInitParameter(name: String): String
    fun getRealPath(path: String): String
}

class ServletAppContextImpl : AppContext {
    @Context 
    private lateinit var servletContext: ServletContext
    override fun getInitParameter(name: String): String {
        return servletContext.getInitParameter(name)
    }
   
    override fun getRealPath(path: String): String {
        return servletContext.getRealPath(path)
    } 
}

class StaticAppContextImpl : AppContext {
    override fun getInitParameter(name: String): String {
        throw UnsupportedOperationException("StaticAppContextImpl does not support getInitParameter")
    }

    override fun getRealPath(path: String): String {
        throw UnsupportedOperationException("StaticAppContextImpl does not support getRealPath")
    }
}