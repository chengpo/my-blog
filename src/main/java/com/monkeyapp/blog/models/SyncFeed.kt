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

import java.text.ParseException
import java.text.SimpleDateFormat

data class SyncFeed(
        val version:String = "2.0",
        val channel: Channel = Channel()
)

data class Channel(
    val title: String = "Monkey Blogger",
    val link: String = "http://monkey-blogger.herokuapp.com",
    val description: String = "Po Cheng's technical blog",
    val items: List<Item> = emptyList()
)

class Item internal constructor(paper: Paper) {
    private val title: String
    private val link: String
    private val description: String
    private var pubDate: String? = null
    private val guid: String

    init {
        title = paper.getFile().getTitle()
        link = "http://monkey-blogger.herokuapp.com#!/posts/" + paper.getFile().getUrl()
        guid = link
        description = paper.getContent().toString() + "<p> ... </p>"
        try {
            val date = SimpleDateFormat("yyyy/MM/dd hh:mm")
                    .parse(paper.getFile().getCreationTime())
            pubDate = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
            pubDate = null
        }
    }
}