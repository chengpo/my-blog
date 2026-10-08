package com.monkeyapp.blog.static

import com.monkeyapp.blog.di.RootScope
import com.monkeyapp.blog.di.StaticRootScopeImpl
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class WebsiteGenerator(
    private val baseDir: String,
    private val appVersion: String
) {
    private val logger = LoggerFactory.getLogger(WebsiteGenerator::class.java)
    private val threadPoolExecutor = Executors.newFixedThreadPool(50)
    private val rootScope: RootScope = StaticRootScopeImpl()

    fun build() {
        logger.info("Generating static website in $baseDir with app version $appVersion ...")

        val component = Component()

        listOf(
            PostResourceGenerator(component),
            PageResourceGenerator(component),
            FeedResourceGenerator(component)
        )
            .map { it.start() }
            .run { CompletableFuture.allOf(*toTypedArray()) }
            .join() // Wait for all tasks to complete)

        try {
            threadPoolExecutor.shutdown()
            threadPoolExecutor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS)
            logger.info("Static website generation completed.")
        } catch (e: InterruptedException) {
            logger.error("Thread pool executor interrupted: ${e.message}")
            Thread.currentThread().interrupt()
        }
    }

    private inner class Component :
        PostResourceGenerator.Dependencies,
        PageResourceGenerator.Dependencies,
        FeedResourceGenerator.Dependencies {
        
        override fun distDir() = "${baseDir}/static-dist"
        
        override fun executor() = threadPoolExecutor as Executor
        
        override fun postController() = rootScope.visitorScope().postController()
        override fun pageController() = rootScope.visitorScope().pageController()
        override fun feedController() = rootScope.visitorScope().feedController()
    }
}
