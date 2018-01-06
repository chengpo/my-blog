/*
MIT License

Copyright (c) 2018 Po Cheng

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

package com.monkeyapp.blog;

import com.monkeyapp.blog.readers.TextReader;
import com.monkeyapp.blog.wrappers.FileWrapper;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileWrapperImpl implements FileWrapper {
    private static final Pattern NAME_PATTERN = Pattern.compile("^(\\d{4})-(\\d{4})-(\\d{4})-(\\w+)-(.+)\\.md$");

    private final Logger logger;
    private final String realPath;
    private final String name;

    private final String year;
    private final String monthday;
    private final String time;
    private final String tag;
    private final String title;
    private final long priority;
    private final boolean isPaper;

    public FileWrapperImpl(String realPath) {
        this.logger = Logger.getLogger(FileWrapperImpl.class);
        this.realPath = realPath;

        this.name = new File(realPath).getName();

        final Matcher matcher = NAME_PATTERN.matcher(this.name);
        if (matcher.matches()) {
            this.year =  matcher.group(1);
            this.monthday = matcher.group(2);
            this.time = matcher.group(3);
            this.tag = matcher.group(4);
            this.title = matcher.group(5);
            this.priority = Long.valueOf(this.year) * 10000L * 10000L +
                    Long.valueOf(this.monthday) * 10000L +
                    Long.valueOf(this.time);
            this.isPaper = true;
        } else {
            this.year = "";
            this.monthday = "";
            this.time = "";
            this.tag = "";
            this.title = "";
            this.priority = 0;
            this.isPaper = false;
        }
    }

    @Override
    public String getYear() {
        return year;
    }

    @Override
    public String getMonthday() {
        return monthday;
    }

    @Override
    public String getTime() {
        return time;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getPriority() {
        return priority;
    }

    @Override
    public boolean isPaper() {
        return isPaper;
    }

    @Override
    public Optional<String> completeRead() {
        return TextReader.completeRead(realPath);
    }

    @Override
    public Optional<String> partialRead() {
        return TextReader.partialRead(realPath);
    }
}
