package com.monkeyapp.blog.rest.module;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    public List<Entity> getPostEntities(){
        return getPostEntities(null);
    }

    @Nonnull
    public List<Entity> getPostEntities(String tag) {
        return postFileNames.stream()
                            .map(Entity::fromFileName)
                            .filter((post) -> tag == null || tag.isEmpty() || post.getTag().equals(tag))
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
    }

    @Nonnull
    public List<String> getPostTags() {
        return postFileNames.stream()
                            .map(Entity::fromFileName)
                            .map(Entity::getTag)
                            .distinct()
                            .sorted()
                            .collect(Collectors.toList());
    }

    private static Optional<List<String>> fromJson(InputStream input) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            return Optional.of(new ObjectMapper().readValue(reader, new TypeReference<List<String>>() { }));
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
