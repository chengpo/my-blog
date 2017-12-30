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

@Deprecated
public class PostIdRepositoryImpl implements PostIdRepository {
    private final List<String> postFileNames;

    public PostIdRepositoryImpl(List<String> postFileNames) {
        this.postFileNames = Collections.unmodifiableList(postFileNames);
    }

    @Override
    public Stream<PaperId> getAllPostIds() {
        return getPostIds(noneFilter(), noneFilter());
    }

    @Override
    public Stream<PaperId> getPostIdsByTag(String tag) {
        return getPostIds(noneFilter(), filterByTag(tag));
    }

    @Override
    public Stream<PaperId> getPostIdsByName(String year, String monthDay, String title) {
        return getPostIds(filterByName(year, monthDay, title), noneFilter());
    }

    @Override
    public List<TagCounter> getPostTags() {
        final Comparator<TagCounter> byTag = Comparator.comparing(TagCounter::getTag);
        return getPostIds(noneFilter(), noneFilter())
                .map(PaperId::getTag)
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new TagCounter(entry.getKey(), entry.getValue()))
                .sorted(byTag)
                .collect(Collectors.toList());
    }

    private Stream<PaperId> getPostIds(Predicate<String> baseOnName,
                                       Predicate<PaperId> baseOnTag) {
        return postFileNames.parallelStream()
                            .filter(baseOnName)
                            .map(PaperId::fromFileName)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .filter(baseOnTag);
    }

    private static <T> Predicate<T> noneFilter() {
        return (T t) -> true;
    }

    private static Predicate<String> filterByName(String year, String monthDay, String title) {
        return name ->
                    name.startsWith(String.format("%s-%s", year, monthDay)) &&
                    name.endsWith(String.format("%s.md", title));
    }

    private static Predicate<PaperId> filterByTag(String tag) {
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
