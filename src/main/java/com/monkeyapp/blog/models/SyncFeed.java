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

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "rss")
public class SyncFeed {
    @XmlAttribute(name = "version")
    private final String version = "2.0";

    @XmlElement(name = "channel")
    private final Channel channel = new Channel();

    SyncFeed setItems(List<Item> items) {
        channel.items = Collections.unmodifiableList(items);
        return this;
    }

    List<Item> getItems() {
        return channel.items;
    }

    static final class Channel {
        @XmlElement(name = "title")
        private final String title = "Monkey Blogger";

        @XmlElement(name = "link")
        private final String link = "http://monkey-blogger.herokuapp.com";

        @XmlElement(name = "description")
        private final String description = "Po Cheng's technical blog";

        @XmlElement(name = "item")
        private List<Item> items = Collections.emptyList();
    }

    static final class Item {
        @XmlElement(name = "title")
        private final String title;

        @XmlElement(name = "link")
        private final String link;

        @XmlElement(name = "description")
        private final String description;

        @XmlElement(name = "pubDate")
        private String pubDate;

        @XmlElement(name = "guid")
        private final String guid;

        Item(Paper paper) {
            this.title = paper.getId().getTitle();
            this.link = "http://monkey-blogger.herokuapp.com/posts/" + paper.getId().getUrl();
            this.guid = this.link;
            this.description = paper.getContent() + "<p> ... </p>";

            try {
                Date date = new SimpleDateFormat("yyyy/MM/dd hh:mm")
                                    .parse(paper.getId().getCreationTime());

                this.pubDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                this.pubDate = null;
            }
        }
    }
}
