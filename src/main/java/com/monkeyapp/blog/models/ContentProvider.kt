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
        return inputStreamProvider
            .streamOf(path)
            .run(::InputStreamReader)
            .run(::BufferedReader)
            .use {
                it.lines().collect(Collectors.joining(System.lineSeparator()))
            }
            .run(htmlFormatter::format)
    }
}

class PartialContentProvider(private val inputStreamProvider: InputStreamProvider,
                             private val blogParameters: BlogParameters) {
    private val htmlFormatter by lazy { HtmlFormatter() }

    fun contentOf(path: String):  String {
        return inputStreamProvider
            .streamOf(path)
            .run(::InputStreamReader)
            .run(::BufferedReader)
            .use {
                it.lines().limit(blogParameters.partialFileLines()).collect(Collectors.joining(System.lineSeparator()))
            }
            .run(htmlFormatter::format)
    }
}

class HtmlFormatter {
    private val parser = Parser.builder().build()
    private val renderer = HtmlRenderer.builder().build()
    
    fun format(markDownContent: String): String {
        return parser.parse(markDownContent).run(renderer::render)
    }
}
