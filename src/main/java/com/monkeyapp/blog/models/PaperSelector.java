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

import java.util.function.Predicate;
import java.util.regex.Pattern;

class PaperSelector {
    public static Predicate<String> all() {
        return (fileName) -> true;
    }

    public static Predicate<String> byDate(String year, String monthDay) {
        return (fileName) -> {
            Pattern tagPattern = Pattern.compile(
                                        String.format("^(%s)-(%s)-(\\d{4})-(\\w+)-(.+)\\.md$", year, monthDay),
                                        Pattern.CASE_INSENSITIVE);

            return tagPattern.matcher(fileName).matches();
        };
    }

    public static Predicate<String> byTitle(String title) {
        return (fileName) -> {
            Pattern tagPattern = Pattern.compile(
                                        String.format("^(\\d{4})-(\\d{4})-(\\d{4})-(\\w+)-(%s)\\.md$", title),
                                        Pattern.CASE_INSENSITIVE);

            return tagPattern.matcher(fileName).matches();
        };
    }

    public static Predicate<String> byTag(String tag) {
        return (fileName) -> {
            if (tag.isEmpty()) {
                return true;
            }

            Pattern tagPattern = Pattern.compile(
                                        String.format("^(\\d{4})-(\\d{4})-(\\d{4})-(%s)-(.+)\\.md$", tag),
                                        Pattern.CASE_INSENSITIVE);

            return tagPattern.matcher(fileName).matches();
        };
    }

}
