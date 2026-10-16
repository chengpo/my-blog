package com.monkeyapp.blog.static

import com.monkeyapp.blog.StaticUrl
import com.monkeyapp.blog.di.BlogParameters
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class HomePageGenerator(dependencies: Dependencies): StaticResourceGenerator {
    private val blogParameters = dependencies.blogParameters()
    
    private val baseDir = dependencies.baseDir()
    private val distDir = dependencies.distDir()
    private val executor = dependencies.executor()
    
    private val logger = LoggerFactory.getLogger(PostResourceGenerator::class.java)

    override fun start(): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync({
            generateHomePage()
            null    
        }, executor)
    }
    
    private fun generateHomePage() {
        val homePageTemplatePath = Path.of("${baseDir}/src/main/webapp/index.template.html")
        val notFoundTemplatePath = Path.of("${baseDir}/src/main/webapp/404.template.html")

        val homePagePath = Path.of("${distDir}/index.html")
        val notFoundPagePath = Path.of("${distDir}/404.html")
        
        try {
            var homePageContent = Files.readString(homePageTemplatePath)
            val params = mapOf(
                "site-title" to blogParameters.siteTitle(),
                "static-libs" to "${StaticUrl.VERSION}/libs",
                "version" to StaticUrl.VERSION
            ) 
            
            params.forEach { 
                (key, value) ->
                homePageContent = homePageContent.replace("{{$key}}", value)
            }

            Files.writeString(homePagePath, homePageContent)
            
            val notFoundPageConent = Files.readString(notFoundTemplatePath)
            Files.writeString(notFoundPagePath, notFoundPageConent)
            
            logger.info("Static home page conent generated")
        } catch (e: IOException) {
            logger.error("Error writing static home page: ${e.message}")
        }
    }

    interface Dependencies {
       fun blogParameters(): BlogParameters
       fun baseDir(): String
       fun distDir(): String
       fun executor(): Executor
    }
}