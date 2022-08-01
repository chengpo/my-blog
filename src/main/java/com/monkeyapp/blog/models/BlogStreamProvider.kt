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
                   .run(inputStreamProvider::streamOf)
                   .run(::InputStreamReader)
                   .run(::BufferedReader)
                   .use { 
                       it.lines().collect(Collectors.joining(System.lineSeparator())) 
                   }
                   .run(BlogStreamProvider::toMetaStream)
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

