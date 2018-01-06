package com.monkeyapp.blog.wrappers;

import java.util.List;

public interface StorageWrapper {
    List<String> listPostFiles();
    List<String> listPageFiles();
    FileWrapper openPostFile(String file);
    FileWrapper openPageFile(String file);
}
