package com.monkeyapp.blog.static

import com.monkeyapp.blog.controllers.SitemapController
import com.monkeyapp.blog.dtos.SitemapDto
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.StringWriter
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import javax.xml.bind.JAXBContext

class SitemapResourceGenerator(dependencies: Dependencies) : StaticResourceGenerator {
    private val distDir = dependencies.distDir()
    private val sitemapController = dependencies.sitemapController()
    private val executor = dependencies.executor()

    private val logger = LoggerFactory.getLogger(SitemapResourceGenerator::class.java)

    override fun start(): CompletableFuture<Void> {
        return CompletableFuture.supplyAsync({ sitemapController.sitemap() }, executor)
            .thenAcceptAsync(this::generateSitemapContent, executor)
    }

    private fun generateSitemapContent(sitemapDto: SitemapDto) {
        logger.info("Generating static sitemap content")
        val sitemapFilePath = Path.of("${distDir}/sitemap.xml")
        try {
            val context = JAXBContext.newInstance(SitemapDto::class.java)
            val marshaller = context.createMarshaller()
            val writer = StringWriter()
            marshaller.marshal(sitemapDto, writer)
            Files.writeString(sitemapFilePath, writer.toString())
            logger.info("Static sitemap generated: ${sitemapFilePath.fileName}")
        } catch (e: IOException) {
            logger.error("Error writing sitemap to file: ${e.message}")
        }
    }

    interface Dependencies {
        fun distDir(): String
        fun sitemapController(): SitemapController
        fun executor(): Executor
    }
}
