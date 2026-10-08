package com.monkeyapp.blog.di

import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service
import javax.inject.Inject
import javax.ws.rs.core.Context

@Contract
interface BlogParameters {
    fun postPerChunk(): Long
    fun partialFileLines(): Long
    fun siteTitle(): String
}

@Service
class BlogParametersImpl : BlogParameters {
    @Inject
    private lateinit var context: AppContext

    override fun postPerChunk(): Long {
        return context.getInitParameter(POST_PER_CHUNK_PARAM).toLong()
    }

    override fun partialFileLines(): Long {
        return context.getInitParameter(PARTIAL_FILE_LINES_PARAM).toLong()
    }

    override fun siteTitle(): String {
        return context.getInitParameter(SITE_TITLE_PARAM)
    }

    companion object {
        private const val POST_PER_CHUNK_PARAM = "post-per-chunk"
        private const val PARTIAL_FILE_LINES_PARAM = "partial-file-lines"
        private const val SITE_TITLE_PARAM = "site-title"
    }
}
