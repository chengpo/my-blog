package com.monkeyapp.blog.di

import org.jvnet.hk2.annotations.Contract
import org.jvnet.hk2.annotations.Service
import javax.inject.Inject

@Contract
interface BlogParameters {
    fun partialFileLines(): Long
    fun siteTitle(): String
}

@Service
class BlogParametersImpl : BlogParameters {
    @Inject
    private lateinit var context: AppContext

    override fun partialFileLines(): Long = context.getInitParameter(PARTIAL_FILE_LINES_PARAM).toLong()

    override fun siteTitle(): String = context.getInitParameter(SITE_TITLE_PARAM)
    

    companion object {
        private const val POST_PER_CHUNK_PARAM = "post-per-chunk"
        private const val PARTIAL_FILE_LINES_PARAM = "partial-file-lines"
        private const val SITE_TITLE_PARAM = "site-title"
    }
}

class StaticBlogParametersImpl : BlogParameters {
    override fun partialFileLines(): Long = 3L

    override fun siteTitle(): String = "Monkey Blogger - Some Random Thought"
}
