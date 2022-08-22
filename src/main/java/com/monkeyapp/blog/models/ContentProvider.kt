package com.monkeyapp.blog.models

import com.monkeyapp.blog.di.BlogParameters
import com.monkeyapp.blog.di.InputStreamProvider
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

class ContentProvider(private val inputStreamProvider: InputStreamProvider,
                      private val contentReader: ContentReader) {
    private val htmlFormatter by lazy { HtmlFormatter() }

    fun contentOf(path: String):  String {
        return inputStreamProvider
            .streamOf(path)
            .let(::InputStreamReader)
            .let(::BufferedReader)
            .use(contentReader::read)
            .let(htmlFormatter::format)
    }
}

abstract class ContentReader {
    abstract fun read (reader: BufferedReader): String
}

class CompleteContentReader : ContentReader() {
    override fun read(reader: BufferedReader): String {
        return reader
            .lines()
            .collect(Collectors.joining(System.lineSeparator()))
    }
}

class PartialContentReader(private val blogParameters: BlogParameters): ContentReader() {
    override fun read(reader: BufferedReader): String {
        return reader
            .lines()
            .limit(blogParameters.partialFileLines())
            .collect(Collectors.joining(System.lineSeparator()))
    }
}


class HtmlFormatter {
    private val parser = Parser.builder().build()
    private val renderer = HtmlRenderer.builder().build()
    
    fun format(markdownContent: String): String {
        return parser.parse(markdownContent).run(renderer::render)
    }
}
