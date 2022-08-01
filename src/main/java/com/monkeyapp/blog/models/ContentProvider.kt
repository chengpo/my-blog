package com.monkeyapp.blog.models

import com.monkeyapp.blog.di.BlogParameters
import com.monkeyapp.blog.di.InputStreamProvider
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

class CompleteContentProvider(private val inputStreamProvider: InputStreamProvider) {
    private val htmlFormatter by lazy { HtmlFormatter() }

    fun contentOf(path: String):  String {
        val inputStream = inputStreamProvider.streamOf(path)
        val markDownContent = BufferedReader(InputStreamReader(inputStream)).use {
            it.lines().collect(Collectors.joining(System.lineSeparator()))
        }
        return htmlFormatter.format(markDownContent)
    }
}

class PartialContentProvider(private val inputStreamProvider: InputStreamProvider,
                             private val blogParameters: BlogParameters) {
    private val htmlFormatter by lazy { HtmlFormatter() }

    fun contentOf(path: String):  String {
        val inputStream = inputStreamProvider.streamOf(path)
        val markDownContent = BufferedReader(InputStreamReader(inputStream)).use {
            it.lines().limit(blogParameters.partialFileLines()).collect(Collectors.joining(System.lineSeparator()))
        }
        return htmlFormatter.format(markDownContent)
    }
}

class HtmlFormatter {
    private val parser = Parser.builder().build()
    private val renderer = HtmlRenderer.builder().build()
    
    fun format(markDownContent: String): String {
        return parser.parse(markDownContent).apply(renderer::render)
    }
}
