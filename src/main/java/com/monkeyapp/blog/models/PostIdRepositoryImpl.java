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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PostIdRepositoryImpl implements PostIdRepository {
    private static final Predicate<String> ALL_NAMES = name -> true;
    private static final Predicate<Paper.Id> ALL_TAGS = id -> true;

    private final List<String> postFileNames;

    public PostIdRepositoryImpl(List<String> postFileNames) {
        this.postFileNames = Collections.unmodifiableList(postFileNames);
    }

    @Override
    public Stream<Paper.Id> getAllPostIds() {
        return getPostIds(ALL_NAMES, ALL_TAGS);
    }

    @Override
    public Stream<Paper.Id> getPostIdsByTag(String tag) {
        return getPostIds(ALL_NAMES, filterByTag(tag));
    }

    @Override
    public Stream<Paper.Id> getPostIdsByName(String year, String monthDay, String title) {
        return getPostIds(filterByName(year, monthDay, title), ALL_TAGS);
    }

    @Override
    public List<TagViewModel> getPostTags() {
        return getPostIds(ALL_NAMES, ALL_TAGS)
                .map(Paper.Id::getTag)
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new TagViewModel(entry.getKey(), entry.getValue()))
                .sorted()
                .collect(Collectors.toList());
    }

    private Stream<Paper.Id> getPostIds(Predicate<String> baseOnName,
                                        Predicate<Paper.Id> baseOnTag) {
        return postFileNames.parallelStream()
                            .filter(baseOnName)
                            .map(Paper.Id::fromFileName)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .filter(baseOnTag);
    }

    private static Predicate<String> filterByName(String year, String monthDay, String title) {
        return name ->
                    name.startsWith(String.format("%s-%s", year, monthDay)) &&
                    name.endsWith(String.format("%s.md", title));
    }

    private static Predicate<Paper.Id> filterByTag(String tag) {
        return id ->
                    tag.isEmpty() ||
                    tag.equalsIgnoreCase(id.getTag());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PostIdRepository --- ");
        sb.append(postFileNames.size()).append(" posts in total:\n");
        sb.append("[\n");

        postFileNames.forEach(post -> sb.append(post).append("\n"));

        sb.append("]\n");
        return sb.toString();
    }
}
