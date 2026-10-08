package com.monkeyapp.blog.static

import com.monkeyapp.blog.controllers.PostController

import org.slf4j.LoggerFactory
import org.slf4j.Logger

class PostResourceBuilder(private val controller: PostController) {

    private val logger: Logger = LoggerFactory.getLogger(PostResourceBuilder::class.java)

    fun build() {
        val postChunk = controller.postChunk()
        postChunk.posts.forEach { post ->
            logger.info("Generating static post: ${post.metadata.name} ...")

            post.metadata.url.split("/").let { urlParts ->
                val year = urlParts[1]
                val monthday = urlParts[2]
                val title = urlParts[3]

                controller.postContent(year, monthday, title)
                    .ifPresent { postDto ->
                        logger.info("Generating static post content: ${postDto.content.substring(0, 20) } ...")
                    
                }
            }
        }
    }
}