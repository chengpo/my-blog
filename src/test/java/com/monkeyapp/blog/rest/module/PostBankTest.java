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
                "  \"2017-0418-1053-tag2-post3.md\",\n" +
                "  \"2017-0418-1153-tag2-post2.md\",\n" +
                "  \"2018-0418-1205-tag1-post1.md\"\n" +
                "]").getBytes());

        postBank = new PostBank(input);
    }

    @Test
    public void testPostListOrderByCreatedTime() {
        assertEquals("wrong post list size", 3, postBank.getPosts().size());

        assertEquals("post 1 should be on the top of post list",
                     "Post1",
                     postBank.getPosts().get(0).getTitle());

        assertEquals("post 2 should be in the middle of post list",
                "Post2",
                postBank.getPosts().get(1).getTitle());

        assertEquals("post 3 should be on the end of post list",
                "Post3",
                postBank.getPosts().get(2).getTitle());
    }

    @Test
    public void testFilterPostListByTag() {
        assertEquals("wrong post list size for tag1", 1, postBank.getPosts("tag1").size());
        assertEquals("post 1 should listed for tag1",
                     "Post1",
                     postBank.getPosts("tag1").get(0).getTitle());

        assertEquals("wrong post list size for tag2", 2, postBank.getPosts("tag2").size());
        assertEquals("post 2 should the first of tag2 list",
                "Post2",
                postBank.getPosts("tag2").get(0).getTitle());
        assertEquals("post 3 should the second of tag2 list",
                "Post3",
                postBank.getPosts("tag2").get(1).getTitle());
    }
}
