package com.monkeyapp.blog.assets

import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

fun AssetFile.readMarkdownContent(reader: AssetFile.() -> String): String {
    val parser = Parser.builder().build()
    val document = parser.parse(this.reader())
    val renderer = HtmlRenderer.builder().build()
    return renderer.render(document)
}

val AssetFile.isMarkdown : Boolean
    get() = path.endsWith(".md")