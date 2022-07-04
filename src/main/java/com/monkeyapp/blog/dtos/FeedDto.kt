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
    @XmlAttribute(name = "version")
    var version: String = "",

    @JvmField
    @XmlElement(name = "channel")
    var channel: ChannelDto? = null
)

data class ChannelDto(
    @XmlElement(name = "title")
    var title: String = "",

    @XmlElement(name = "link")
    var link: String = "",

    @XmlElement(name = "description")
    var description: String = "",

    @XmlElement(name = "item")
    var items: List<ItemDto> = emptyList()
)

data class ItemDto(
    @XmlElement(name = "title")
    var title: String = "",

    @XmlElement(name = "link")
    var link: String = "",

    @XmlElement(name = "description")
    var description: String = "",

    @XmlElement(name = "pubDate")
    var pubDate: String = "",

    @XmlElement(name = "guid")
    var guid: String = ""
)
