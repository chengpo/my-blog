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
        val pagesDirFuture = CompletableFuture.supplyAsync(this::createPagesDir, executor)
        
        return pagesDirFuture.thenCompose { pagesDirPath ->
            val allPageFutures = listOf(ABOUT_MYSELF)
                .map { pageTitle ->
                    CompletableFuture.supplyAsync({pageController.pageContent(pageTitle) }, executor)
                    .thenAcceptAsync({ pageDto ->
                        if(pageDto.isPresent) generatePage(pageDto.get(), pagesDirPath)
                    }, executor)
            }
            
            CompletableFuture.allOf(*allPageFutures.toTypedArray())    
        }
    }
    
    private fun createPagesDir(): Path {
        val path = Path.of("${distDir}/pages")
        Files.createDirectories(path)
        return path
    }
    
    private fun generatePage(pageDto: PageDto, pagesDirPath: Path) {
        logger.info("Generating static page (${pagesDirPath}) ...") 
        
        val pageFilePath = Path.of("${pagesDirPath}/${pageDto.metadata.name}.json")
        try {
            Files.writeString(pageFilePath, jacksonObjectMapper().writeValueAsString(pageDto))
            logger.info("Static page content generated: ${pageFilePath.fileName}")
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