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
package com.monkeyapp.blog.models

import com.monkeyapp.blog.wrappers.FileWrapper
import com.monkeyapp.blog.wrappers.StorageWrapper
import java.util.*
import java.util.function.Function
import java.util.function.Predicate
import java.util.function.Supplier
import java.util.stream.Collectors

class PaperRepository(private val storage: StorageWrapper) {
    fun getPostPaperChunk(tag: String, offset: Int, chunkCapacity: Int): PaperChunk {
        val byTag = Predicate { file: FileWrapper ->
            tag.isEmpty() ||
                    tag.equals(file.tag, ignoreCase = true)
        }
        val postFiles = Supplier {
            storage.listPostFiles()
                    .parallelStream()
                    .map { file: String? -> storage.openPostFile(file!!) }
                    .filter(byTag.and(FileWrapper::isPaper))
        }
        val byPriority = Comparator.comparingLong(FileWrapper::priority)
                .reversed()
        val papers = postFiles.get()
                .sorted(byPriority)
                .map { fileWrapper: FileWrapper? -> PaperFile(fileWrapper!!) }
                .skip(offset.toLong())
                .map { obj: PaperFile? -> Paper.partialPaper() }
                .limit(chunkCapacity.toLong())
                .filter(Predicate<Optional<Paper?>> { obj: Optional<Paper?> -> obj.isPresent })
                .map(Function<Optional<Paper?>, Paper> { obj: Optional<Paper?> -> obj.get() })
                .collect(Collectors.toList())
        val eof = offset + papers.size >= postFiles.get().count()
        return PaperChunk(papers, offset, chunkCapacity, eof)
    }

    fun getCompletePost(year: String?, monthDay: String?, title: String?): Optional<Paper> {
        val bySelector = Predicate { file: FileWrapper ->
            file.isPaper &&
                    file.year.equalsIgnoreCase(year) &&
                    file.monthday.equalsIgnoreCase(monthDay) &&
                    file.title.equalsIgnoreCase(title)
        }
        return storage.listPostFiles()
                .parallelStream()
                .map { file: String? -> storage.openPostFile(file!!) }
                .filter(bySelector)
                .findFirst()
                .map { fileWrapper: FileWrapper? -> PaperFile(fileWrapper!!) }
                .map { obj: PaperFile? -> Paper.completePaper() }
                .filter(Predicate<Optional<Paper?>> { obj: Optional<Paper?> -> obj.isPresent })
                .map(Function<Optional<Paper?>, Paper> { obj: Optional<Paper?> -> obj.get() })
    }

    fun getCompletePage(title: String?): Optional<Paper> {
        val bySelector = Predicate { file: FileWrapper ->
            file.isPaper &&
                    file.title.equalsIgnoreCase(title)
        }
        return storage.listPageFiles()
                .parallelStream()
                .map { file: String? -> storage.openPageFile(file!!) }
                .filter(bySelector)
                .findFirst()
                .map { fileWrapper: FileWrapper? -> PaperFile(fileWrapper!!) }
                .map { obj: PaperFile? -> Paper.completePaper() }
                .filter(Predicate<Optional<Paper?>> { obj: Optional<Paper?> -> obj.isPresent })
                .map(Function<Optional<Paper?>, Paper> { obj: Optional<Paper?> -> obj.get() })
    }

    val postTags: List<TagCounter>
        get() {
            val byTag = Comparator.comparing(TagCounter::tag)
            return storage.listPostFiles()
                    .parallelStream()
                    .map { file: String? -> storage.openPostFile(file!!) }
                    .map { fileWrapper: FileWrapper? -> PaperFile(fileWrapper!!) }
                    .map<Any>(PaperFile::getTag)
                    .collect(Collectors.groupingBy<Any, Any?, Any, Long>(Function { tag: Any? -> tag }, Collectors.counting()))
                    .entries
                    .stream()
                    .map { entry: Map.Entry<Any?, Long> -> TagCounter(entry.key, entry.value) }
                    .sorted(byTag)
                    .collect(Collectors.toList())
        }

}