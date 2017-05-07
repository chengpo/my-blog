package com.monkeyapp.blog.rest.module;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownReader implements Reader {
    private final Reader reader;

    public MarkdownReader(Reader reader) {
        this.reader = reader;
    }

    @Override
    public String read() {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(reader.read());
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }
}
