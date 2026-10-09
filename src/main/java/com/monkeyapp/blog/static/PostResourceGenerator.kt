package com.monkeyapp.blog.static

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.monkeyapp.blog.controllers.PostController
import com.monkeyapp.blog.dtos.PostChunkDto
import com.monkeyapp.blog.dtos.PostDto
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class PostResourceGenerator(dependencies: Dependencies) : StaticResourceGenerator  {
    private val distDir = dependencies.distDir()
    private val postController = dependencies.postController()
    private val executor = dependencies.executor()
    
    private val logger = LoggerFactory.getLogger(PostResourceGenerator::class.java)

    override fun start(): CompletableFuture<Void> {
        val postsDirFuture = CompletableFuture.supplyAsync(this::createPostsDir, executor)
        val postChunkFuture = CompletableFuture.supplyAsync(postController::postChunk, executor)

        return CompletableFuture.allOf(postsDirFuture, postChunkFuture)
            .thenCompose {
                val postsDirPath = postsDirFuture.join()
                val postChunk = postChunkFuture.join()

                val postChunkFuture =
                    CompletableFuture.supplyAsync({ generatePostChunk(postChunk, postsDirPath) }, executor)

                val allPostFutures = postChunk.posts.map { post ->
                    CompletableFuture
                        .supplyAsync({ getPostContent(post) }, executor)
                        .thenAcceptAsync({ postDto ->
                            if (postDto.isPresent) generatePostContent(postDto.get(), postsDirPath)
                        }, executor)
                }

                CompletableFuture.allOf(postChunkFuture, *allPostFutures.toTypedArray())
            }
    }

    private fun createPostsDir(): Path {
        val path = Path.of("${distDir}/posts")
        Files.createDirectories(path)
        return path
    }

    private fun getPostContent(post: PostDto): Optional<PostDto> {
        val urlParts = post.metadata.url.split("/")
        val year = urlParts[1]
        val monthday = urlParts[2]
        val title = urlParts[3]

        return postController.postContent(year, monthday, title)
    }

    private fun generatePostContent(postDto: PostDto, postsDirPath: Path) {
        logger.info("Generating static post (${postsDirPath}): ${postDto.metadata.name} ...")


        val postFilePath = Path.of("${postsDirPath}/${postDto.metadata.name}.json")

        try {
            Files.writeString(postFilePath, jacksonObjectMapper().writeValueAsString(postDto))
            logger.info("Static post content generated: ${postFilePath.fileName}")
        } catch (e: Exception) {
            logger.error("Error writing static post content to file: ${e.message}")
        }
    }

    private fun generatePostChunk(postChunk: PostChunkDto, postsDirPath: Path) {
        logger.info("Generating static post list (${postsDirPath}) ...")

        val postChunkFilePath = Path.of("${postsDirPath}/post-chunk.json")

        try {
            Files.writeString(postChunkFilePath, jacksonObjectMapper().writeValueAsString(postChunk))
            logger.info("Static post chunk generated: ${postChunkFilePath.fileName}")
        } catch (e: IOException) {
            logger.error("Error writing static post chunk to file: ${e.message}")
        }
    }
    
    interface Dependencies {
        fun distDir(): String
        fun executor(): Executor
        fun postController(): PostController
    }
}