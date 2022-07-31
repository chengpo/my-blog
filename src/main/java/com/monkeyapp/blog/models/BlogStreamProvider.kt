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
                         inputStreamProvider: InputStreamProvider,
                         private val fileListJson: String = "file-list.json") {

    private val inputStreamOf by lazy { inputStreamProvider.inputStreamOf() }

    fun metaStream(): Stream<BlogMetadata> {
        val blogMetadataFactory = BlogMetadataFactory()

        val jsonString = BufferedReader(InputStreamReader(inputStreamOf(pathOf(root, fileListJson)))).use {
            it.lines().collect(Collectors.joining(System.lineSeparator()))
        }

        return ObjectMapper()
            .readValue(
                jsonString,
                object : TypeReference<List<String>>() {})
            .mapNotNull { name ->
                blogMetadataFactory.create(path = pathOf(root, name), name = name)
            }.stream()
    }

    private fun pathOf(root: String, file: String): String {
        return context.getRealPath("$root/$file")
    }
}

