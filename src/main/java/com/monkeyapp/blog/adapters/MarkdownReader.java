/*
MIT License

Copyright (c) 2017 Po Cheng

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

package com.monkeyapp.blog.adapters;

import com.monkeyapp.blog.models.Paper;
import com.monkeyapp.blog.models.PaperId;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Optional;
import java.util.function.Function;

class MarkdownReader {
    private final PaperId id;
    private Function<String, String> pathTransform;

    static MarkdownReader from(PaperId id) {
        return new MarkdownReader(id);
    }

    MarkdownReader with(Function<String, String> pathTransform) {
        this.pathTransform = pathTransform;
        return this;
    }

    Optional<Paper> by(Function<String, Optional<String>> textReader) {
        return textReader.apply(pathTransform.apply(id.getName()))
                .map((text) -> {
                    final Parser parser = Parser.builder().build();
                    final Node document = parser.parse(text);
                    final HtmlRenderer renderer = HtmlRenderer.builder().build();
                    return renderer.render(document);
                })
                .map(markdown -> new Paper(id, markdown));
    }

    private MarkdownReader(PaperId id) {
        this.id = id;
    }
}
