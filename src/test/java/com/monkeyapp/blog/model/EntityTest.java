package com.monkeyapp.blog.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class EntityTest {
    @Test
    public void testCreatePostInstanceWithValidFileName() {
        final String validFilename = "2017-0418-1053-tag-arbitrary-post-title.md";

        Entity post = Entity.fromFileName(validFilename);
        assertNotNull("failed to recreate post fromFileName valid file name " + validFilename, post);

        assertEquals("wrong post tag", "tag", post.getTag());
        assertEquals("wrong post name", "Arbitrary Post Title", post.getTitle());
        assertEquals("wrong post id", 201704181053L, post.getId());

        assertEquals("wrong post file name", validFilename, post.getName());
    }

    @Test
    public void testCreatePostInstanceWithInvalidFileName() {
        final String invalidFileName = "201704181053-tag-arbitrary-post-title.md";
        Entity post = Entity.fromFileName(invalidFileName);
        assertNull("should not create post instance fromFileName invalid file name " + invalidFileName, post);
    }
}
