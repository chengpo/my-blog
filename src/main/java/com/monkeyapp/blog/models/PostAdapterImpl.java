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

import com.monkeyapp.blog.AppContext;
import com.monkeyapp.blog.reader.MarkdownReader;
import com.monkeyapp.blog.reader.AbstractReader;
import com.monkeyapp.blog.reader.TextReader;

import java.util.Optional;

public class PostAdapterImpl implements PostAdapter {
    @Override
    public Optional<Post> toPartialPost(Entity entity) {
        final String path = AppContext.getRealPostPath(entity.getName());
        return readPost(TextReader.partialReader(path), entity);
    }

    @Override
    public Optional<Post> toCompletePost(Entity entity) {
        final String path = AppContext.getRealPostPath(entity.getName());
        return readPost(TextReader.completeReader(path), entity);
    }

    @Override
    public Optional<Post> toCompletePage(Entity entity) {
        final String path = AppContext.getRealPagePath(entity.getName());
        return readPost(TextReader.completeReader(path), entity);
    }

    private static Optional<Post> readPost(AbstractReader reader, Entity entity) {
        return new MarkdownReader(reader)
                .read()
                .map((content) -> new Post(entity, content));
    }
}
