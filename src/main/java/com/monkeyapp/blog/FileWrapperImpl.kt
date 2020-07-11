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
package com.monkeyapp.blog

import com.monkeyapp.blog.wrappers.FileWrapper
import java.io.File
import java.util.regex.Pattern

class FileWrapperImpl(override val realPath: String) : FileWrapper {
    override val name: String
    override var year: String? = null
    override var monthday: String? = null
    override var time: String? = null
    override var tag: String? = null
    override var title: String? = null
    override var isPaper = false

    companion object {
        private val NAME_PATTERN = Pattern.compile("^(\\d{4})-(\\d{4})-(\\d{4})-(\\w+)-(.+)\\.md$")
    }

    init {
        name = File(realPath).name
        val matcher = NAME_PATTERN.matcher(name)
        if (matcher.matches()) {
            year = matcher.group(1)
            monthday = matcher.group(2)
            time = matcher.group(3)
            tag = matcher.group(4)
            title = matcher.group(5)
            isPaper = true
        } else {
            year = ""
            monthday = ""
            time = ""
            tag = ""
            title = ""
            isPaper = false
        }
    }
}