/*
MIT License

Copyright (c) 2017 - 2022 Po Cheng

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
package com.monkeyapp.blog.dtos

import javax.xml.bind.annotation.XmlAttribute
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "rss")
data class SyncFeedDto(
    @JvmField
    @field:XmlAttribute(name = "version")
    var version: String = "2.0",

    @JvmField
    @field:XmlElement(name = "channel")
    var channel: FeedChannelDto? = null
)

data class FeedChannelDto(
    @JvmField
    @field:XmlElement(name = "title")
    val title: String,

    @JvmField
    @field:XmlElement(name = "link")
    val link: String,

    @JvmField
    @field:XmlElement(name = "description")
    val description: String,

    @JvmField
    @field:XmlElement(name = "item")
    val items: List<FeedItemDto>
)

data class FeedItemDto(
    @JvmField
    @field:XmlElement(name = "title")
    val title: String,

    @JvmField
    @field:XmlElement(name = "link")
    val link: String,

    @JvmField
    @field:XmlElement(name = "description")
    val description: String,

    @JvmField
    @field:XmlElement(name = "pubDate")
    val pubDate: String,

    @JvmField
    @field:XmlElement(name = "guid")
    val guid: String
)
