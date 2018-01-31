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

package com.monkeyapp.blog;

import com.monkeyapp.blog.wrappers.FileWrapper;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileWrapperImpl implements FileWrapper {
    private static final Pattern NAME_PATTERN = Pattern.compile("^(\\d{4})-(\\d{4})-(\\d{4})-(\\w+)-(.+)\\.md$");

    private final String realPath;
    private final String name;

    private final String year;
    private final String monthday;
    private final String time;
    private final String tag;
    private final String title;
    private final boolean isPaper;

    public FileWrapperImpl(String realPath) {
        this.realPath = realPath;

        this.name = new File(realPath).getName();

        final Matcher matcher = NAME_PATTERN.matcher(this.name);
        if (matcher.matches()) {
            this.year =  matcher.group(1);
            this.monthday = matcher.group(2);
            this.time = matcher.group(3);
            this.tag = matcher.group(4);
            this.title = matcher.group(5);
            this.isPaper = true;
        } else {
            this.year = "";
            this.monthday = "";
            this.time = "";
            this.tag = "";
            this.title = "";
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
    public String getRealPath() {
        return realPath;
    }

    @Override
    public boolean isPaper() {
        return isPaper;
    }
}
