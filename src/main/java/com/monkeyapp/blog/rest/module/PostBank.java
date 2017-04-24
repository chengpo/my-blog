package com.monkeyapp.blog.rest.module;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class PostBank {
    private final List<String> postFileNames;

    public PostBank() {
        this(Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("posts/file-list.json"));
    }

    PostBank(@Nullable InputStream input) {
        postFileNames = Optional.ofNullable(input)
                                .flatMap(PostBank::fromJson)
                                .orElseGet(Collections::emptyList);
    }

    @Nonnull
    public List<Post> getPostList() {
        return postFileNames.stream()
                            .map(Post::from)
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
    }

    private static Optional<List<String>> fromJson(InputStream input) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            return Optional.of(new Gson().fromJson(reader, new TypeToken<ArrayList<String>>(){}.getType()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PostBank --- ");
        sb.append(String.valueOf(postFileNames.size())).append(" posts in total:\n");
        sb.append("[\n");

        postFileNames.forEach(post -> sb.append(post).append("\n"));

        sb.append("]\n");
        return sb.toString();
    }
}
