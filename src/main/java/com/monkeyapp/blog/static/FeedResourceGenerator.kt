package com.monkeyapp.blog.static

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.monkeyapp.blog.controllers.FeedController
import com.monkeyapp.blog.dtos.SyncFeedDto
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class FeedResourceGenerator(dependencies: Dependencies) : StaticResourceGenerator {
    private val distDir = dependencies.distDir()
    private val feedController = dependencies.feedController()
    private val executor = dependencies.executor()
    
    private val logger = LoggerFactory.getLogger(FeedResourceGenerator::class.java)
    
    override fun start(): CompletableFuture<Void> {
        val feedDirFuture = CompletableFuture.supplyAsync(this::createFeedDir, executor)
        val syncFeedFuture = CompletableFuture.supplyAsync({ feedController.feed() }, executor)
        
        return CompletableFuture.allOf(feedDirFuture, syncFeedFuture)
            .thenCompose {
               val feedDirPath = feedDirFuture.join() 
               val syncFeed = syncFeedFuture.join()
               
               CompletableFuture.runAsync({ generateFeedContent(syncFeed, feedDirPath) }, executor)
            }
    }
    
    private fun createFeedDir(): Path {
        val path = Path.of("${distDir}/feed")
        Files.createDirectories(path)
        return path
    }
    
    private fun generateFeedContent(syncFeedDto: SyncFeedDto, feedDirPath: Path) {
        logger.info("Generating static sync feed content $feedDirPath ...")
        
        val syncFeedFilePath = Path.of("${feedDirPath}/sync-feed.json")
        try {
           Files.writeString(syncFeedFilePath, jacksonObjectMapper().writeValueAsString(syncFeedDto)) 
           logger.info("Static sync feed generated: ${syncFeedFilePath.fileName}")
        } catch (e: IOException) {
            logger.error("Error writing sync feed to file : ${e.message}")
        }
    }
    
    
    interface Dependencies {
        fun distDir(): String
        fun feedController(): FeedController
        fun executor(): Executor
    }
}