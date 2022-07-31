package com.monkeyapp.blog.models

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
        return htmlFormatter.toHtml(markDownContent)
    }
}

class PartialContentProvider(private val inputStreamProvider: InputStreamProvider,
                             private val partialFileLines: Long = 32L) {
    private val htmlFormatter by lazy { HtmlFormatter() }

    fun contentOf(path: String):  String {
        val inputStream = inputStreamProvider.streamOf(path)
        val markDownContent = BufferedReader(InputStreamReader(inputStream)).use {
            it.lines().limit(partialFileLines).collect(Collectors.joining(System.lineSeparator()))
        }
        return htmlFormatter.toHtml(markDownContent)
    }
}

class HtmlFormatter {
    private val renderer = HtmlRenderer.builder().build()

    fun toHtml(markDownContent: String): String {
        val parser = Parser.builder().build()
        val document = parser.parse(markDownContent)
        return renderer.render(document)
    }
}
