package com.monkeyapp.blog.wrappers;

import java.util.Optional;

public interface FileWrapper {
    String getName();
    String getYear();
    String getMonthday();
    String getTime();
    String getTag();
    String getTitle();
    long getPriority();
    boolean isPaper();
    Optional<String> completeRead();
    Optional<String> partialRead();
}
