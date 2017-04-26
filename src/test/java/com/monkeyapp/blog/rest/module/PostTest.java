package com.monkeyapp.blog.rest.module;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PostTest {
    @Test
    public void testCreatePostInstanceWithValidFileName() {
        final String validFilename = "2017-0418-1053-tag-arbitrary-post-title.md";

        Post post = Post.from(validFilename);
        assertNotNull("failed to recreate post from valid file name " + validFilename, post);

        assertEquals("wrong post year", "2017", post.getYear());
        assertEquals("wrong post day", "0418", post.getDay());
        assertEquals("wrong post time", "1053", post.getTime());
        assertEquals("wrong post tag", "tag", post.getTag());
        assertEquals("wrong post name", "arbitrary-post-title", post.getTitle());
        assertEquals("wrong post id", 201704181053L, post.getId());

        assertEquals("wrong post file name", validFilename, post.toFileName());
    }

    @Test
    public void testCreatePostInstanceWithInvalidFileName() {
        final String invalidFileName = "201704181053-tag-arbitrary-post-title.md";
        Post post = Post.from(invalidFileName);
        assertNull("should not create post instance from invalid file name " + invalidFileName, post);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildThrowsExceptionForIncompleteBuild(){
        new Post.Builder().build();
    }
}
