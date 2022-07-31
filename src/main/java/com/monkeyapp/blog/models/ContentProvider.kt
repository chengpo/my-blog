package com.monkeyapp.blog.models

import com.monkeyapp.blog.di.InputStreamProvider
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

class CompleteContentProvider(inputStreamProvider: InputStreamProvider) {
    private val inputStreamOf by lazy { inputStreamProvider.inputStreamOf() }
    private val htmlFormatter by lazy { HtmlFormatter() }

    fun contentOf(path: String):  String {
        val markDownContent = BufferedReader(InputStreamReader(inputStreamOf(path))).use {
            it.lines().collect(Collectors.joining(System.lineSeparator()))
        }

        return htmlFormatter.toHtml(markDownContent)
    }
}


class PartialContentProvider(inputStreamProvider: InputStreamProvider, private val partialFileLines: Long = 32L) {
    private val inputStreamOf by lazy { inputStreamProvider.inputStreamOf() }
    private val htmlFormatter by lazy { HtmlFormatter() }

    fun contentOf(path: String):  String {
        val markDownContent = BufferedReader(InputStreamReader(inputStreamOf(path))).use {
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