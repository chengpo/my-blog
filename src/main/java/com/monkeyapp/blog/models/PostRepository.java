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

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class PostRepository {
    private final List<String> postFileNames;

    public PostRepository(List<String> postFileNames) {
        this.postFileNames = postFileNames;
    }

    public Stream<Paper.Id> getPostIds(){
        return getPostIds(null);
    }

    public Stream<Paper.Id> getPostIds(String tag) {
        final Predicate<Paper.Id> baseOnTag = (tag == null || tag.isEmpty()) ?
                                            (entity) -> true :
                                            (entity -> entity.getTag().equals(tag));

        return postFileNames.stream()
                            .map(Paper.Id::fromFileName)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .filter(baseOnTag);
    }

    public Stream<Paper.Id> getPostIds(String year, String monthDay, String title) {
        return postFileNames.stream()
                .filter((fileName) ->
                        fileName.startsWith(String.format("%s-%s", year, monthDay)) &&
                                fileName.endsWith(String.format("%s.md", title)))
                .map(Paper.Id::fromFileName)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Stream<String> getPostTags() {
        return postFileNames.stream()
                            .map(Paper.Id::fromFileName)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(Paper.Id::getTag)
                            .distinct()
                            .sorted();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PostRepository --- ");
        sb.append(String.valueOf(postFileNames.size())).append(" posts in total:\n");
        sb.append("[\n");

        postFileNames.forEach(post -> sb.append(post).append("\n"));

        sb.append("]\n");
        return sb.toString();
    }
}
