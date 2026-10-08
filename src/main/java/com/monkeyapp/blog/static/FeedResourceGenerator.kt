package com.monkeyapp.blog.static

import com.monkeyapp.blog.controllers.FeedController
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class FeedResourceGenerator(dependencies: Dependencies) : StaticResourceGenerator {
    private val distDir = dependencies.distDir()
    private val feedController = dependencies.feedController()
    private val executor = dependencies.executor()
    
    private val logger = LoggerFactory.getLogger(FeedResourceGenerator::class.java)
    
    override fun start(): CompletableFuture<Void> {
        
        return TODO("Provide the return value")
    }
    
    interface Dependencies {
        fun distDir(): String
        fun feedController(): FeedController
        fun executor(): Executor
    }
}