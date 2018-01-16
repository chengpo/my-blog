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

package com.monkeyapp.blog.dtos;

import lombok.NonNull;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "rss")
@Setter
public class SyncFeedDto {
    @XmlAttribute(name = "version")
    @NonNull
    private String version;

    @XmlElement(name = "channel")
    @NonNull
    ChannelDto channel;

    @Setter
    public static final class ChannelDto {
        @XmlElement(name = "title")
        @NonNull
        private String title;

        @XmlElement(name = "link")
        @NonNull
        private String link;

        @XmlElement(name = "description")
        @NonNull
        private String description;

        @XmlElement(name = "item")
        @NonNull
        private List<ItemDto> items;
    }

    @Setter
    public static final class ItemDto {
        @XmlElement(name = "title")
        @NonNull
        private String title;

        @XmlElement(name = "link")
        @NonNull
        private String link;

        @XmlElement(name = "description")
        @NonNull
        private String description;

        @XmlElement(name = "pubDate")
        @NonNull
        private String pubDate;

        @XmlElement(name = "guid")
        @NonNull
        private String guid;
    }
}
