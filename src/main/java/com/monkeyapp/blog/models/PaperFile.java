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
import com.monkeyapp.blog.wrappers.FileWrapper;
import com.monkeyapp.blog.wrappers.ReadableWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class PaperFile implements ReadableWrapper {
    @JsonProperty("creationTime")
    private final String creationTime;

    @JsonProperty("url")
    private final String url;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("tag")
    private final String tag;

    @JsonIgnore
    private final FileWrapper fileWrapper;

    PaperFile(FileWrapper fileWrapper) {
        this.fileWrapper = fileWrapper;

        final String year = fileWrapper.getYear();
        final String month = fileWrapper.getMonthday().substring(0, 2);
        final String day = fileWrapper.getMonthday().substring(2);
        final String hour = fileWrapper.getTime().substring(0, 2);
        final String minute = fileWrapper.getTime().substring(2);

        this.creationTime = String.format("%s/%s/%s %s:%s", year, month, day, hour, minute);

        this.url = String.format("%s/%s/%s",
                                fileWrapper.getYear(),
                                fileWrapper.getMonthday(),
                                fileWrapper.getTitle());

        this.title = Arrays.stream(fileWrapper.getTitle().split("-"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));

        this.tag = WordUtils.capitalize(fileWrapper.getTag());
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public FileWrapper getFileWrapper() {
        return fileWrapper;
    }

    @Override
    public String toString() {
        return String.format("paper(creationTime = %s, tag = %s, title = %s)", creationTime, tag, title);
    }
}
