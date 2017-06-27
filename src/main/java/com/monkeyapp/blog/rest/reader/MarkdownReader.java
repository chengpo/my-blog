package com.monkeyapp.blog.rest.reader;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownReader extends AbstractReader {
    private final AbstractReader reader;

    public MarkdownReader(AbstractReader reader) {
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
