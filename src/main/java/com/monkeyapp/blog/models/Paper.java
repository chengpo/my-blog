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

package com.monkeyapp.blog.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.monkeyapp.blog.readers.MarkdownReader;

import java.util.Optional;
import java.util.function.Function;

public class Paper {
    @JsonProperty("file")
    private final PaperFile file;

    @JsonProperty("content")
    private final String content;

    public Paper(PaperFile file, String content) {
        this.file = file;
        this.content = content;
    }

    public static Optional<Paper> completePaper(PaperFile file) {
        return toPaper(PaperFile::completeRead)
                .apply(file);
    }

    public static Optional<Paper> partialPaper(PaperFile file) {
        return toPaper(PaperFile::partialRead)
                .apply(file);
    }

    private static Function<PaperFile, Optional<Paper>> toPaper(Function<PaperFile, Optional<String>> reader) {
        return (file) -> reader
                .apply(file)
                .map(MarkdownReader::read)
                .map((content) -> new Paper(file, content));
    }

    public PaperFile getFile() {
        return file;
    }

    public String getContent() {
        return content;
    }

}