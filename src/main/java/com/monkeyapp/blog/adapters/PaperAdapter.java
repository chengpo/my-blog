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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkeyapp.blog.AppContext;
import com.monkeyapp.blog.models.Paper;
import com.monkeyapp.blog.models.PaperId;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class PaperAdapter {
    public Optional<List<String>> toPostFileNames(String jsonPath) {
        return toFileNames(AppContext::realPostPath, jsonPath);
    }

    private Optional<List<String>> toFileNames(UnaryOperator<String> realPath, String jsonPath) {
        return TextReader
                .completeRead(realPath.apply(jsonPath))
                .map(json -> {
                    try {
                        return new ObjectMapper()
                                .readValue(json, new TypeReference<List<String>>() {
                                });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                });
    }

    public Optional<Paper> toPartialPost(PaperId id) {
        return MarkdownReader.from(id)
                .with(AppContext::realPostPath)
                .by(TextReader::partialRead);
    }

    public Optional<Paper> toCompletePost(PaperId id) {
        return MarkdownReader.from(id)
                .with(AppContext::realPostPath)
                .by(TextReader::completeRead);
    }

    public Optional<Paper> toCompletePage(PaperId id) {
        return MarkdownReader.from(id)
                .with(AppContext::realPagePath)
                .by(TextReader::completeRead);
    }
}
