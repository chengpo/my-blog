/*
MIT License

Copyright (c) 2017 - 2018 Po Cheng

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

import com.monkeyapp.blog.wrappers.FileWrapper;
import com.monkeyapp.blog.wrappers.StorageWrapper;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PaperRepository {
    private StorageWrapper storage;

    public PaperRepository(StorageWrapper storage) {
        this.storage = storage;
    }

    public PaperChunk getPostPaperChunk(String tag, int offset, int chunkCapacity) {
        final Predicate<FileWrapper> byTag =
                (file) -> tag.isEmpty() ||
                          tag.equalsIgnoreCase(file.getTag());

        final Supplier<Stream<FileWrapper>> postFiles =
                () -> storage.listPostFiles()
                            .parallelStream()
                            .map(storage::openPostFile)
                            .filter(byTag.and(FileWrapper::isPaper));

        final Comparator<FileWrapper> byPriority =
                Comparator.comparingLong(FileWrapper::getPriority)
                          .reversed();

        final List<Paper> papers = postFiles.get()
                .sorted(byPriority)
                .map(PaperFile::new)
                .skip(offset)
                .map(Paper::partialPaper)
                .limit(chunkCapacity)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        final boolean eof = offset + papers.size() >= postFiles.get().count();
        return new PaperChunk(papers, offset, chunkCapacity, eof);
    }

    public Optional<Paper> getCompletePost(String year, String monthDay, String title) {
        final Predicate<FileWrapper> bySelector =
                (file) -> file.isPaper() &&
                          file.getYear().equalsIgnoreCase(year) &&
                          file.getMonthday().equalsIgnoreCase(monthDay) &&
                          file.getTitle().equalsIgnoreCase(title);

        return storage.listPostFiles()
                .parallelStream()
                .map(storage::openPostFile)
                .filter(bySelector)
                .findFirst()
                .map(PaperFile::new)
                .map(Paper::completePaper)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public Optional<Paper> getCompletePage(String title) {
        final Predicate<FileWrapper> bySelector =
                (file) -> file.isPaper() &&
                          file.getTitle().equalsIgnoreCase(title);

        return storage.listPageFiles()
                .parallelStream()
                .map(storage::openPageFile)
                .filter(bySelector)
                .findFirst()
                .map(PaperFile::new)
                .map(Paper::completePaper)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    public List<TagCounter> getPostTags() {
        final Comparator<TagCounter> byTag = Comparator.comparing(TagCounter::getTag);

        return storage.listPostFiles()
                .parallelStream()
                .map(storage::openPostFile)
                .map(PaperFile::new)
                .map(PaperFile::getTag)
                .collect(Collectors.groupingBy(tag -> tag, Collectors.counting()))
                .entrySet()
                .stream()
                .map(entry -> new TagCounter(entry.getKey(), entry.getValue()))
                .sorted(byTag)
                .collect(Collectors.toList());
    }
}
