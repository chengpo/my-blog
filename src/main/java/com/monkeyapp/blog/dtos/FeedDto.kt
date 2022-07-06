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

import jakarta.xml.bind.annotation.XmlAttribute
import jakarta.xml.bind.annotation.XmlElement
import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "rss")
data class SyncFeedDto(
    @JvmField
    @XmlAttribute(name = "version")
    val version: String,

    @JvmField
    @XmlElement(name = "channel")
    val channel: FeedChannelDto
)

data class FeedChannelDto(
    @JvmField
    @XmlElement(name = "title")
    val title: String,

    @JvmField
    @XmlElement(name = "link")
    val link: String,

    @JvmField
    @XmlElement(name = "description")
    val description: String,

    @JvmField
    @XmlElement(name = "item")
    val items: List<FeedItemDto>
)

data class FeedItemDto(
    @JvmField
    @XmlElement(name = "title")
    val title: String,

    @JvmField
    @XmlElement(name = "link")
    val link: String,

    @JvmField
    @XmlElement(name = "description")
    val description: String,

    @JvmField
    @XmlElement(name = "pubDate")
    val pubDate: String,

    @JvmField
    @XmlElement(name = "guid")
    val guid: String
)
