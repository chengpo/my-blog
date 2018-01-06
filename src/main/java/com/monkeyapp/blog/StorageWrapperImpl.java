package com.monkeyapp.blog;

import com.monkeyapp.blog.readers.FileListReader;
import com.monkeyapp.blog.wrappers.FileWrapper;
import com.monkeyapp.blog.wrappers.StorageWrapper;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class StorageWrapperImpl implements StorageWrapper {
    @Override
    public List<String> listPostFiles() {
        return loadFileList(AppContext::realPostPath)
                .apply("file-list.json");
    }

    @Override
    public List<String> listPageFiles() {
        return loadFileList(AppContext::realPagePath)
                .apply("file-list.json");
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
