/*
MIT License

Copyright (c) 2017 - 2018 Po Cheng

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.monkeyapp.blog.readers

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.util.regex.Pattern
import java.util.stream.Collectors

open class AssetFile(val path: String) {
    open val inputStream: InputStream
        get() = File(path).inputStream()

    val priority: Long
        get() = 0

    val metadata: Metadata by lazy {
        Metadata(path)
    }

    fun readPartialContent(): String {
        return BufferedReader(InputStreamReader(inputStream)).use {
            it.lines().limit(MAX_PARTIAL_FILE_LINES).collect(Collectors.joining(System.lineSeparator()))
        }
    }

    fun readFullContent(): String {
        return BufferedReader(InputStreamReader(inputStream)).use {
            it.lines().collect(Collectors.joining(System.lineSeparator()))
        }
    }

    open val childAssets: List<AssetFile>
        get() {
            return ObjectMapper()
                    .readValue<List<String>>(readFullContent(), object : TypeReference<List<String>>() {})
                    .map(::AssetFile)
        }

    class Metadata(path: String) {
        val title: String
        val tag: String

        val year: String
        val monthDay: String

        init {
            title = ""
            tag = ""

            year = ""
            monthDay = ""
        }

        companion object {
            private val NAME_PATTERN = Pattern.compile("^(\\d{4})-(\\d{4})-(\\d{4})-(\\w+)-(.+)\\.md$")
        }
    }

    companion object {
        private const val MAX_PARTIAL_FILE_LINES = 32L
    }
}

fun AssetFile.readMarkdownContent(reader: AssetFile.() -> String): String {
    val parser = Parser.builder().build()
    val document = parser.parse(reader())
    val renderer = HtmlRenderer.builder().build()
    return renderer.render(document)
}

val AssetFile.isMarkdown : Boolean
        get() = path.endsWith(".md")

class AssetRepository(private val postRegister: AssetFile,
                      private val pageRegister: AssetFile) {

    fun getPostList(offset: Int = 0,
                    tag: String = "",
                    maxPosts: Int = Int.MAX_VALUE): List<AssetFile> {

        return postRegister.childAssets
                .asSequence()
                .filter(AssetFile::isMarkdown)
                .filter { tag.isEmpty() ||  it.metadata.tag == tag }
                .sortedByDescending(AssetFile::priority)
                .drop(offset)
                .take(maxPosts)
                .toList()
    }

    fun getPost(year: String, monthDay: String, title: String): AssetFile? {
        if (year.isEmpty() || monthDay.isEmpty() || title.isEmpty()) {
            return null
        }

        return postRegister.childAssets
                .firstOrNull {
                    it.isMarkdown &&
                    year == it.metadata.year &&
                    monthDay == it.metadata.monthDay &&
                    title.equals(it.metadata.title, ignoreCase = true)
                }
    }

    fun getPage(title: String): AssetFile? {
        if (title.isEmpty()) {
            return null
        }

        return pageRegister.childAssets
                .firstOrNull {
                    it.isMarkdown &&
                    title.equals(it.metadata.title, ignoreCase = true)
                }
    }

    val tags: List<Pair<String, Int>>
        get() {
            return postRegister.childAssets
                    .map(AssetFile::metadata)
                    .groupBy(AssetFile.Metadata::tag)
                    .map{ it.key to it.value.size }
        }
}