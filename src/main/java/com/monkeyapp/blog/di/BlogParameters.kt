package com.monkeyapp.blog.di

import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service
import javax.servlet.ServletContext
import javax.ws.rs.core.Context

@Contract
interface BlogParameters {
    fun postPerChunk(): Long
}

@Service
class BlogParametersImpl : BlogParameters {
    @Context
    private lateinit var context: ServletContext

    override fun postPerChunk(): Long {
        return context.getInitParameter(POST_PER_CHUNK_PARAM).toLong()
    }

    companion object {
        private const val POST_PER_CHUNK_PARAM = "post-per-chunk"
    }
}
