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

package com.monkeyapp.blog.wrappers;

import com.monkeyapp.blog.readers.TextReader;

import java.util.Optional;

public interface FileWrapper {
    String getName();
    String getYear();
    String getMonthday();
    String getTime();
    String getTag();
    String getTitle();
    boolean isPaper();
    String getRealPath();

    default long getPriority() {
        return isPaper() ?
                Long.valueOf(getYear()) * 10000L * 10000L +
                        Long.valueOf(getMonthday()) * 10000L +
                        Long.valueOf(getTime())
                : 0L;
    }

    default Optional<String> completeRead() {
        return TextReader.completeRead(getRealPath());
    }

    default Optional<String> partialRead() {
        return TextReader.partialRead(getRealPath());
    }
}
