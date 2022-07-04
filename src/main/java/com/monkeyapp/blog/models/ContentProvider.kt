package com.monkeyapp.blog.models

import com.monkeyapp.blog.di.InputStreamProvider
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

interface CompleteContentProvider {
    fun completeContentOf(): (String) -> String
}

class CompleteContentProviderImpl(inputStreamProvider: InputStreamProvider) : CompleteContentProvider {
    private val inputStreamOf = inputStreamProvider.inputStreamOf()

    override fun completeContentOf(): (String) -> String {
        return { path ->
            HtmlFormatter().toHtml {
                BufferedReader(InputStreamReader(inputStreamOf(path))).use {
                    it.lines().collect(Collectors.joining(System.lineSeparator()))
                }
            }
        }
    }
}

interface PartialContentProvider {
    fun partialContentOf(): (String) -> String
}

class PartialContentProviderImpl(inputStreamProvider: InputStreamProvider) : PartialContentProvider {
    private val inputStreamOf = inputStreamProvider.inputStreamOf()

    override fun partialContentOf(): (String) -> String {
        return { path ->
            HtmlFormatter().toHtml {
                BufferedReader(InputStreamReader(inputStreamOf(path))).use {
                    it.lines().limit(MAX_PARTIAL_FILE_LINES).collect(Collectors.joining(System.lineSeparator()))
                }
            }
        }
    }

    companion object {
        private const val MAX_PARTIAL_FILE_LINES = 32L
    }
}

class HtmlFormatter {
    private val renderer = HtmlRenderer.builder().build()

    fun toHtml(markDownContent: () -> String): String {
        val parser = Parser.builder().build()
        val document = parser.parse(markDownContent())
        return renderer.render(document)
    }
}