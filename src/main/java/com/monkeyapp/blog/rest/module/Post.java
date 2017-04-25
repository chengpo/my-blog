package com.monkeyapp.blog.rest.module;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@XmlRootElement
public class Post implements Comparable<Post> {
    private static final int POST_FIELD_NUM = 5;
    private static final Pattern NAME_PATTERN = Pattern.compile("(\\d{4})-(\\d{4})-(\\d{4})-(\\w+)-(.+)\\.md");


    @XmlElement(name="year")
    private final String year;

    @XmlElement(name="day")
    private final String day;

    @XmlElement(name="time")
    private final String time;

    @XmlElement(name="tag")
    private final String tag;

    @XmlElement(name="title")
    private final String title;

    @XmlElement(name="id")
    private final long id;

    public static class Builder {
        private Map<String, String> fields= new HashMap<>(POST_FIELD_NUM);

        public Builder setYear(String year) {
            fields.put("year", year);
            return this;
        }

        public Builder setDay(String day) {
            fields.put("day", day);
            return this;
        }

        public Builder setTime(String time) {
            fields.put("time", time);
            return this;
        }

        public Builder setTag(String tag) {
            fields.put("tag", tag);
            return this;
        }

        public Builder setTitle(String title) {
            fields.put("title", title);
            return this;
        }

        public Post build() {
            if (fields.size() < POST_FIELD_NUM) {
                throw new IllegalStateException("inadequate fields: " + this);
            }

            return new Post(fields);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder("[ ");
            fields.keySet().forEach(name -> sb.append(name).append(" "));
            sb.append("]");
            return sb.toString();
        }
    }

    static Post from(String fileName) {
        Matcher matcher = NAME_PATTERN.matcher(fileName);
        if (matcher.matches()) {
            return new Post(new HashMap<String, String>(POST_FIELD_NUM) {
                {
                    put("year", matcher.group(1));
                    put("day", matcher.group(2));
                    put("time", matcher.group(3));
                    put("tag", matcher.group(4));
                    put("title", matcher.group(5));
                }
            });
        }

        return null;
    }

    private Post(Map<String, String> fields) {
        year = fields.get("year");
        day = fields.get("day");
        time = fields.get("time");
        tag = fields.get("tag");
        title = fields.get("title");

        id = Long.valueOf(year) * 10000L * 10000L +
             Long.valueOf(day) * 10000L +
             Long.valueOf(time);
    }

    public String getYear() {
        return year;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }

    public String toFileName() {
        return new StringBuilder()
                .append(String.valueOf(year))
                .append("-")
                .append(String.valueOf(day))
                .append("-")
                .append(String.valueOf(time))
                .append("-")
                .append(tag)
                .append("-")
                .append(title)
                .append(".md")
                .toString();
    }

    @Override
    public int compareTo(Post other) {
        return this.id > other.id ? 1 : this.id == other.id ? 0 : -1;
    }

    @Override
    public String toString() {
        return "Local file name: " + toFileName();
    }
}
