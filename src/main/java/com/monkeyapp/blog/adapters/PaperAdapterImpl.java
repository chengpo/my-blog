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

import com.monkeyapp.blog.AppContext;
import com.monkeyapp.blog.models.Paper;

import java.util.Optional;
import java.util.function.Function;

public class PaperAdapterImpl implements PaperAdapter {
    @Override
    public Optional<Paper> toPartialPost(Paper.Id id) {
        return from(id)
                .with(AppContext::realPostPath)
                .by(TextReader::partialRead);
    }

    @Override
    public Optional<Paper> toCompletePost(Paper.Id id) {
        return from(id)
                .with(AppContext::realPostPath)
                .by(TextReader::completeRead);
    }

    @Override
    public Optional<Paper> toCompletePage(Paper.Id id) {
        return from(id)
                .with(AppContext::realPagePath)
                .by(TextReader::completeRead);
    }

    private PaperBuilder from(Paper.Id id) {
        return new PaperBuilder(id);
    }

    private static class PaperBuilder {
        private Paper.Id id;
        private Function<String, String> path;

        private PaperBuilder(Paper.Id id) {
            this.id = id;
        }

        private PaperBuilder with(Function<String, String> path){
            this.path = path;
            return this;
        }

        private Optional<Paper> by(Function<String, AbstractReader> reader) {
            return new MarkdownReader(reader.apply(path.apply(id.getName())))
                        .read()
                        .map((content) -> new Paper(id, content));
        }
    }
}