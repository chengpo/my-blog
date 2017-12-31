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

import com.monkeyapp.blog.adapters.PaperAdapter;

import java.nio.file.FileSystemNotFoundException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.monkeyapp.blog.models.PaperSelector.*;

public class PaperRepositoryImpl implements PaperRepository {
    private final List<String> postFileNames;
    private final PaperAdapter paperAdapter;

    public PaperRepositoryImpl(PaperAdapter paperAdapter) {
        this.paperAdapter = paperAdapter;
        this.postFileNames = paperAdapter
                                .toPostFileNames("file-list.json")
                                .orElseThrow(() -> new FileSystemNotFoundException("Failed to load post file list."));
    }

    @Override
    public PaperChunk getPostsByTag(String tag, int offset, int chunkCapacity) {
        final Supplier<Stream<PaperId>> postIds = () -> selectPosts(byTag(tag));

        final Comparator<PaperId> byPriority = Comparator.comparingLong(PaperId::getPriority)
                                                          .reversed();
        final List<Paper> papers = postIds.get()
                .sorted(byPriority)
                .skip(offset)
                .limit(chunkCapacity)
                .map(paperAdapter::toPartialPost)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        final boolean eof = offset + papers.size() >= postIds.get().count();
        return new PaperChunk(papers, offset, chunkCapacity, eof);
    }

    @Override
    public Optional<Paper> getCompletePost(String year, String monthDay, String title) {
        return selectPosts(byDate(year, monthDay).and(byTitle(title)))
                .findFirst()
                .map(paperAdapter::toCompletePost)
                .map(Optional::get);
    }

    @Override
    public Optional<Paper> getCompletePage(String name) {
        return PaperId.fromFileName(name)
                .map(paperAdapter::toCompletePage)
                .map(Optional::get);
    }

    @Override
    public List<TagCounter> getPostTags() {
        final Comparator<TagCounter> byTag = Comparator.comparing(TagCounter::getTag);
        return selectPosts(all())
                .map(PaperId::getTag)
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new TagCounter(entry.getKey(), entry.getValue()))
                .sorted(byTag)
                .collect(Collectors.toList());
    }

    @Override
    public SyncFeed getPostFeed() {
        final List<SyncFeed.Item> items = getPostsByTag("", 0, Integer.MAX_VALUE)
                                        .getPapers()
                                        .parallelStream()
                                        .map(SyncFeed.Item::new)
                                        .collect(Collectors.toList());

        return new SyncFeed().setItems(items);
    }

    private Stream<PaperId> selectPosts(Predicate<String> selector) {
        return postFileNames.parallelStream()
                .filter(selector)
                .map(PaperId::fromFileName)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }
}
