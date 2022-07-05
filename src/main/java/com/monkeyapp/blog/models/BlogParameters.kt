package com.monkeyapp.blog.models

import jakarta.servlet.ServletContext

interface BlogParameters {
    fun postPerChunk(): Long
}

class BlogParametersImpl(private val context: ServletContext) : BlogParameters {
    override fun postPerChunk(): Long {
        return context.getInitParameter(POST_PER_CHUNK_PARAM).toLong()
    }

    companion object {
        const val POST_PER_CHUNK_PARAM = "post-per-chunk"
    }
}
