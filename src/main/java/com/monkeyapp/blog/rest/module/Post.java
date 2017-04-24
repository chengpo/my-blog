package com.monkeyapp.blog.rest.module;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@XmlRootElement
public class Post implements Comparable<Post> {
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

    static Post from(String filename) {
        Matcher matcher = NAME_PATTERN.matcher(filename);
        if (matcher.matches()) {
            return new Post(matcher);
        }

        return null;
    }

    Post(Matcher matcher) {
        year = matcher.group(1);
        day = matcher.group(2);
        time = matcher.group(3);
        tag = matcher.group(4);
        title = matcher.group(5);

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

    @Override
    public int compareTo(Post other) {
        return this.id > other.id ? 1 : this.id == other.id ? 0 : -1;
    }

    @Override
    public String toString() {
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
}
