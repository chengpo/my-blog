package com.monkeyapp.blog.rest.module;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PostHead implements Comparable<PostHead> {
    private static final int POST_FIELD_NUM = 5;
    private static final Pattern NAME_PATTERN = Pattern.compile("^((\\d{4})-(\\d{4})-(\\d{4})-(\\w+)-(.+))\\.md$");

    @JsonProperty("creationTime")
    private final String creationTime;

    @JsonProperty("name")
    private final String name;

    @JsonProperty("tag")
    private final String tag;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("id")
    private final long id;

    public static PostHead from(String fileName) {
        Matcher matcher = NAME_PATTERN.matcher(fileName);
        if (matcher.matches()) {
            return new PostHead(new HashMap<String, String>(POST_FIELD_NUM) {
                {
                    put("name", matcher.group(1));
                    put("year", matcher.group(2));
                    put("date", matcher.group(3));
                    put("time", matcher.group(4));
                    put("tag", matcher.group(5));
                    put("title", matcher.group(6));
                }
            });
        }

        return null;
    }

    private PostHead(Map<String, String> fields) {
        final String year = fields.get("year");
        final String month = fields.get("date").substring(0, 2);
        final String day = fields.get("date").substring(2);
        final String hour = fields.get("time").substring(0, 2);
        final String minute = fields.get("time").substring(2);

        name = fields.get("name");

        creationTime = String.format("%s/%s/%s %s:%s", year, month, day, hour, minute);

        tag = fields.get("tag");
        title = Arrays.stream(fields.get("title").split("-"))
                      .map(PostHead::capitalize)
                      .collect(Collectors.joining(" "));

        id = Long.valueOf(fields.get("year")) * 10000L * 10000L +
             Long.valueOf(fields.get("date")) * 10000L +
             Long.valueOf(fields.get("time"));
    }

    private static String capitalize(String string) {
        return string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public String toFileName() {
        return name + ".md";
    }

    @Override
    public int compareTo(PostHead other) {
        return this.id > other.id ? 1 : this.id == other.id ? 0 : -1;
    }

    @Override
    public String toString() {
        return "local file name: " + toFileName();
    }
}
