package com.monkeyapp.blog.static

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.monkeyapp.blog.controllers.PageController
import com.monkeyapp.blog.dtos.PageDto
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor

class PageResourceGenerator(dependencies: Dependencies): StaticResourceGenerator {
    private val distDir = dependencies.distDir()
    private val pageController = dependencies.pageController()
    private val executor = dependencies.executor()
    
    private val logger = LoggerFactory.getLogger(PageResourceGenerator::class.java)

    override fun start(): CompletableFuture<Void> {
        val allPageFutures = listOf(ABOUT_MYSELF)
            .map { pageTitle ->
                CompletableFuture.supplyAsync({ pageController.pageContent(pageTitle) }, executor)
                    .thenAcceptAsync({ pageDto ->
                        if (pageDto.isPresent) generatePage(pageDto.get())
                    }, executor)
            }

        return CompletableFuture.allOf(*allPageFutures.toTypedArray())
    }
    
    private fun generatePage(pageDto: PageDto) {
        val pagesDirPath = Path.of("${distDir}/rest/${pageDto.metadata.url}")
        Files.createDirectories(pagesDirPath)
        
        logger.info("Generating static page (${pagesDirPath}) ...") 
        
        val pageFilePath = Path.of("${pagesDirPath}/index.html")
        try {
            Files.writeString(pageFilePath, jacksonObjectMapper().writeValueAsString(pageDto))
            logger.info("Static page content generated: $pageFilePath")
        } catch (e: IOException) {
            logger.error("Error generating static page (${pageFilePath})", e)
        } 
    }
    
    private companion object {
        const val ABOUT_MYSELF = "about-myself"
    }

    interface Dependencies {
       fun distDir(): String
       fun pageController(): PageController
       fun executor(): Executor 
    }    
}