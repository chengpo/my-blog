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

import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Getter
public class SyncFeed {
    private final String version = "2.0";
    private final Channel channel = new Channel();

    @Setter @Getter
    public static final class Channel {
        private final String title = "Monkey Blogger";
        private final String link = "http://monkey-blogger.herokuapp.com";
        private final String description = "Po Cheng's technical blog";
        private List<Item> items;
    }

    @Getter
    public static final class Item {
        private final String title;
        private final String link;
        private final String description;
        private String pubDate;
        private final String guid;

        Item(Paper paper) {
            this.title = paper.getFile().getTitle();
            this.link = "http://monkey-blogger.herokuapp.com/posts/" + paper.getFile().getUrl();
            this.guid = this.link;
            this.description = paper.getContent() + "<p> ... </p>";

            try {
                Date date = new SimpleDateFormat("yyyy/MM/dd hh:mm")
                                    .parse(paper.getFile().getCreationTime());

                this.pubDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z").format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                this.pubDate = null;
            }
        }
    }
}
