package com.monkeyapp.blog.rest.module;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PostBankTest {
    private PostBank postBank;

    @Before
    public void setUp() {
        InputStream input = new ByteArrayInputStream(("[\n" +
                "  \"2017-0418-1053-tag-oldest-post.md\",\n" +
                "  \"2017-0418-1153-tag-newer-post.md\",\n" +
                "  \"2018-0418-1205-tag-newest-post.md\"\n" +
                "]").getBytes());

        postBank = new PostBank(input);
    }

    @Test
    public void testPostListSize() {
        assertEquals("wrong post list size", 3, postBank.getPostList().size());
    }

    @Test
    public void testNewestPostOnTheFirstOfPostList() {
        assertEquals("newest post should be on the top of post list",
                     "newest-post",
                     postBank.getPostList().get(0).getTitle());
    }

    @Test
    public void testNewerPostInTheMiddleOfPostList() {
        assertEquals("newer post should be in the middle of post list",
                "newer-post",
                postBank.getPostList().get(1).getTitle());
    }

    @Test
    public void testOldestPostOnTheEndOfPostList() {
        assertEquals("newest post should be on the end of post list",
                "oldest-post",
                postBank.getPostList().get(2).getTitle());
    }
}
