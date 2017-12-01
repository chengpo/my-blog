/*
MIT License

Copyright (c) 2017 Po Cheng

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

package com.monkeyapp.blog.model;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class PostRepository {
    private final List<String> postFileNames;

    public PostRepository(List<String> postFileNames) {
        this.postFileNames = postFileNames;
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
