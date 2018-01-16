package com.monkeyapp.blog.models;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor @Getter
public class TagCounter {
    @NonNull
    private final String tag;

    private final long count;
}
