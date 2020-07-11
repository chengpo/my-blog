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
package com.monkeyapp.blog

import com.monkeyapp.blog.wrappers.FileWrapper
import com.monkeyapp.blog.wrappers.StorageWrapper
import org.apache.commons.lang3.concurrent.ConcurrentException
import org.apache.commons.lang3.concurrent.LazyInitializer
import java.util.function.Function
import java.util.function.UnaryOperator

class StorageWrapperImpl : StorageWrapper {
    private val postFiles: LazyInitializer<List<String>> = object : LazyInitializer<List<String>>() {
        override fun initialize(): List<String> {
            return loadFileList(UnaryOperator { fileName: String? -> AppContext.realPostPath(fileName) })
                    .apply("file-list.json")
        }
    }
    private val pageFiles: LazyInitializer<List<String>> = object : LazyInitializer<List<String>>() {
        override fun initialize(): List<String> {
            return loadFileList(UnaryOperator { fileName: String? -> AppContext.realPagePath(fileName) })
                    .apply("file-list.json")
        }
    }

    override fun listPostFiles(): List<String> {
        return try {
            postFiles.get()
        } catch (e: ConcurrentException) {
            throw RuntimeException("Failed to load post file list!", e)
        }
    }

    override fun listPageFiles(): List<String> {
        return try {
            pageFiles.get()
        } catch (e: ConcurrentException) {
            throw RuntimeException("Failed to load page file list!", e)
        }
    }

    private fun loadFileList(pathTransform: UnaryOperator<String>): Function<String, List<String>> {
        return Function { path: String ->
            pathTransform
                    .andThen { realPath: String? -> FileWrapperImpl(realPath!!) }
                    .apply(path)
                    .completeRead()
                    .map(Function<String, List<String>> { obj: String -> obj.read() })
                    .orElse(emptyList())
        }
    }

    override fun openPostFile(file: String): FileWrapper {
        return openFile(UnaryOperator { fileName: String? -> AppContext.realPostPath(fileName) })
                .apply(file)
    }

    override fun openPageFile(file: String): FileWrapper {
        return openFile(UnaryOperator { fileName: String? -> AppContext.realPagePath(fileName) })
                .apply(file)
    }

    private fun openFile(pathTransform: UnaryOperator<String>): Function<String, FileWrapper> {
        return Function { path: String ->
            pathTransform
                    .andThen { realPath: String? -> FileWrapperImpl(realPath!!) }
                    .apply(path)
        }
    }
}