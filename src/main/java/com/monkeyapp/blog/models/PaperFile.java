/*
MIT License

Copyright (c) 2017 - 2018 Po Cheng

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

import com.monkeyapp.blog.wrappers.FileWrapper;
import com.monkeyapp.blog.wrappers.ReadableWrapper;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter @ToString
public class PaperFile implements ReadableWrapper {
    private final String creationTime;
    private final String url;
    private final String title;
    private final String tag;

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
}
