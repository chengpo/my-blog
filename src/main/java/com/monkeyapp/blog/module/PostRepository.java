package com.monkeyapp.blog.module;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class PostRepository {
    private final List<String> postFileNames;

    public PostRepository() {
        this(Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("posts" + File.separator + "file-list.json"));
    }

    PostRepository(@Nullable InputStream input) {
        postFileNames = Optional.ofNullable(input)
                                .flatMap(PostRepository::fromJson)
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
                            .filter((entity) -> (entity != null) && (tag == null || tag.isEmpty() || entity.getTag().equals(tag)))
                            .sorted(Comparator.reverseOrder())
                            .collect(Collectors.toList());
    }

    @Nullable
    public Entity getPostEntity(String year, String monthDay, String title) {
        List<Entity> entities = postFileNames.stream()
                                            .filter((fileName) ->
                                                        fileName.startsWith(String.format("%s-%s",year, monthDay)) &&
                                                        fileName.endsWith(String.format("%s.md",title)))
                                            .map(Entity::fromFileName)
                                            .sorted(Comparator.reverseOrder())
                                            .collect(Collectors.toList());

        return entities.isEmpty() ? null : entities.get(0);
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
        sb.append("PostRepository --- ");
        sb.append(String.valueOf(postFileNames.size())).append(" posts in total:\n");
        sb.append("[\n");

        postFileNames.forEach(post -> sb.append(post).append("\n"));

        sb.append("]\n");
        return sb.toString();
    }
}
