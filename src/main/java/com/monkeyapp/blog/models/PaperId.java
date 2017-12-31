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

package com.monkeyapp.blog.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PaperId {
    private static final int POST_FIELD_NUM = 5;
    private static final Pattern NAME_PATTERN = Pattern.compile("^(\\d{4})-(\\d{4})-(\\d{4})-(\\w+)-(.+)\\.md$");

    @JsonProperty("creationTime")
    private final String creationTime;

    @JsonProperty("url")
    private final String url;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("tag")
    private final String tag;

    @JsonIgnore
    private final String name;

    @JsonIgnore
    private final long priority;

    public static Optional<PaperId> fromFileName(String fileName) {
        final Matcher matcher = NAME_PATTERN.matcher(fileName);
        if (matcher.matches()) {
            return Optional.of(new PaperId(new HashMap<String, String>(POST_FIELD_NUM) {
                {
                    put("name", matcher.group(0));
                    put("year", matcher.group(1));
                    put("monthday", matcher.group(2));
                    put("time", matcher.group(3));
                    put("tag", matcher.group(4));
                    put("title", matcher.group(5));
                }
            }));
        }

        return Optional.empty();
    }

    private PaperId(Map<String, String> fields) {
        final String year = fields.get("year");
        final String month = fields.get("monthday").substring(0, 2);
        final String day = fields.get("monthday").substring(2);
        final String hour = fields.get("time").substring(0, 2);
        final String minute = fields.get("time").substring(2);

        this.creationTime = String.format("%s/%s/%s %s:%s", year, month, day, hour, minute);

        this.name = fields.get("name");

        this.url = String.format("%s/%s/%s", fields.get("year"), fields.get("monthday"), fields.get("title"));

        this.title = Arrays.stream(fields.get("title").split("-"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));

        this.tag = WordUtils.capitalize(fields.get("tag"));

        this.priority = Long.valueOf(fields.get("year")) * 10000L * 10000L +
                Long.valueOf(fields.get("monthday")) * 10000L +
                Long.valueOf(fields.get("time"));
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public long getPriority() {
        return priority;
    }

    public String getName() {
        return name;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return String.format("paper(creationTime = %s, tag = %s, title = %s)", creationTime, tag, title);
    }
}
