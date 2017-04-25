package com.monkeyapp.blog.rest.module;

public class PostReader {
    private final Post post;

    public PostReader(Post post) {
        this.post = post;
    }

    public String read() {
        return post.toString();
    }
}
