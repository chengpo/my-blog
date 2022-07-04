package com.monkeyapp.blog.models

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.monkeyapp.blog.di.InputStreamProvider
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.stream.Collectors
import java.util.stream.Stream
import javax.servlet.ServletContext

interface BlogStreamProvider {
    fun total(): Long
    fun metaStream(): Stream<BlogMetadata>

    interface ParentComponent : InputStreamProvider {
        fun context(): ServletContext
    }
}

class BlogStreamProviderImpl(root: String, component: BlogStreamProvider.ParentComponent) : BlogStreamProvider {
    private val root: String
    private val context: ServletContext
    private val inputStreamOf: (String) -> InputStream

    init {
        this.root = root
        this.context = component.context()
        this.inputStreamOf = component.inputStreamOf()
    }

    override fun total(): Long {
        return BufferedReader(InputStreamReader(inputStreamOf(pathOf(root, FILE_LIST_JSON)))).use {
            it.lines().count()
        }
    }

    override fun metaStream(): Stream<BlogMetadata> {
        val blogMetadataFactory = BlogMetadataFactory()

        val jsonString = BufferedReader(InputStreamReader(inputStreamOf(pathOf(root, FILE_LIST_JSON)))).use {
            it.lines().collect(Collectors.joining(System.lineSeparator()))
        }

        return ObjectMapper()
            .readValue<List<String>>(
                jsonString,
                object : TypeReference<List<String>>() {})
            .mapNotNull { name ->
                blogMetadataFactory.create(path = pathOf(root, name), name = name)
            }.stream()
    }

    private fun pathOf(root: String, file: String): String {
        return context.getRealPath("$root/$file")
    }

    companion object {
        const val FILE_LIST_JSON = "file-list.json"
    }
}

