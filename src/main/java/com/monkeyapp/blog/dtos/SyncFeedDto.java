package com.monkeyapp.blog.dtos;

import lombok.Getter;
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
