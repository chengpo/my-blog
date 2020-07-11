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
package com.monkeyapp.blog.models

import com.monkeyapp.blog.wrappers.FileWrapper
import com.monkeyapp.blog.wrappers.ReadableWrapper
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.text.WordUtils
import java.util.*
import java.util.stream.Collectors

class PaperFile internal constructor(override val fileWrapper: FileWrapper) : ReadableWrapper {
    val creationTime: String
    val url: String
    val title: String
    val tag: String

    init {
        val year = fileWrapper.year
        val month = fileWrapper.monthday.substring(0, 2)
        val day = fileWrapper.monthday.substring(2)
        val hour = fileWrapper.time.substring(0, 2)
        val minute = fileWrapper.time.substring(2)
        creationTime = String.format("%s/%s/%s %s:%s", year, month, day, hour, minute)
        url = java.lang.String.format("%s/%s/%s",
                fileWrapper.year,
                fileWrapper.monthday,
                fileWrapper.title)

        title = fileWrapper.title.split("-").stream()
                .map({ str: String? -> StringUtils.capitalize(str) })
                .collect(Collectors.joining(" "))
        tag = WordUtils.capitalize(fileWrapper.tag)
    }
}