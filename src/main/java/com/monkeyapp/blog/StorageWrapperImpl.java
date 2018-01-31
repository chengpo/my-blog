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

package com.monkeyapp.blog;

import com.monkeyapp.blog.readers.FileListReader;
import com.monkeyapp.blog.wrappers.FileWrapper;
import com.monkeyapp.blog.wrappers.StorageWrapper;
import org.apache.commons.lang3.concurrent.ConcurrentException;
import org.apache.commons.lang3.concurrent.LazyInitializer;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class StorageWrapperImpl implements StorageWrapper {
    private final LazyInitializer<List<String>> postFiles = new LazyInitializer<List<String>>() {
        @Override
        protected List<String> initialize() {
            return loadFileList(AppContext::realPostPath)
                    .apply("file-list.json");
        }
    };

    private final LazyInitializer<List<String>> pageFiles = new LazyInitializer<List<String>>() {
        @Override
        protected List<String> initialize() {
            return loadFileList(AppContext::realPagePath)
                    .apply("file-list.json");
        }
    };

    @Override
    public List<String> listPostFiles() {
        try {
            return postFiles.get();
        } catch (ConcurrentException e) {
            throw new RuntimeException("Failed to load post file list!", e);
        }
    }

    @Override
    public List<String> listPageFiles() {
        try {
            return pageFiles.get();
        } catch (ConcurrentException e) {
            throw new RuntimeException("Failed to load page file list!", e);
        }
    }

    private Function<String, List<String>> loadFileList(UnaryOperator<String> pathTransform) {
        return path -> pathTransform
                .andThen(FileWrapperImpl::new)
                .apply(path)
                .completeRead()
                .map(FileListReader::read)
                .orElse(Collections.emptyList());
    }

    @Override
    public FileWrapper openPostFile(String file) {
        return openFile(AppContext::realPostPath)
                .apply(file);
    }

    @Override
    public FileWrapper openPageFile(String file) {
        return openFile(AppContext::realPagePath)
                .apply(file);
    }

    private Function<String, FileWrapper> openFile(UnaryOperator<String> pathTransform) {
        return path -> pathTransform
                .andThen(FileWrapperImpl::new)
                .apply(path);
    }
}
