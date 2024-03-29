package com.monkeyapp.blog.models

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.monkeyapp.blog.di.InputStreamProvider
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.servlet.ServletContext

class BlogStreamProvider(private val root: String,
                         private val context: ServletContext,
                         private val inputStreamProvider: InputStreamProvider,
                         private val fileListJson: String = "file-list.json") {
  
    private val blogMetadataFactory = BlogMetadataFactory()
  
    fun metaStream(): Stream<BlogMetadata> {
        return pathOf(fileListJson)
                   .let(inputStreamProvider::streamOf)
                   .let(::InputStreamReader)
                   .let(::BufferedReader)
                   .use(this::toJsonList)
                   .let(this::toMetaStream)
    }
    
    private fun toJsonList(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining(System.lineSeparator()))
    }
     
    private fun toMetaStream(jsonList: String): Stream<BlogMetadata> {
        return ObjectMapper()
                  .readValue(
                      jsonList,
                      object : TypeReference<List<String>>() {})
                  .map { name ->
                      pathOf(name) to name
                  }
                  .mapNotNull { (path, name) ->
                      blogMetadataFactory.create(path, name)
                  }
                  .stream()
    }
    private fun pathOf(fileName: String): String {
        return context.getRealPath("$root/$fileName")
    }
}

class BlogStreamProviderFactory(dependencies: Dependencies) {
    private val context = dependencies.context()
    private val inputStreamProvider = dependencies.inputStreamProvider()

    fun pageStreamProvider(): BlogStreamProvider {
        return blogStreamProvider(PAGE_ROOT)
    }

    fun postStreamProvider(): BlogStreamProvider {
        return blogStreamProvider(POST_ROOT)
    }

    private fun blogStreamProvider(root: String): BlogStreamProvider {
        return BlogStreamProvider(
            root,
            context,
            inputStreamProvider)
    }

    interface Dependencies {
        fun context(): ServletContext
        fun inputStreamProvider(): InputStreamProvider
    }

    companion object {
        const val POST_ROOT = "/md/posts"
        const val PAGE_ROOT = "/md/pages"
    }
}
