package com.monkeyapp.blog.models;

import junit.framework.AssertionFailedError;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class PaperTest {
    @Test
    public void testCreateIdInstanceWithValidFileName() {
        final String validFilename = "2017-0418-1053-tag-arbitrary-post-title.md";

        Paper.Id id = Paper.Id.fromFileName(validFilename).orElseThrow(() -> new AssertionFailedError("failed to path file name"));

        assertNotNull("failed to recreate paper id with valid file name " + validFilename, id);

        assertEquals("wrong post tag", "tag", id.getTag());
        assertEquals("wrong post name", "Arbitrary Post Title", id.getTitle());
        assertEquals("wrong post priority", 201704181053L, id.getPriority());

        assertEquals("wrong post file name", validFilename, id.getName());
    }

    @Test
    public void testCreateIdInstanceWithInvalidFileName() {
        final String invalidFileName = "201704181053-tag-arbitrary-post-title.md";
        assertFalse("should not create paper id instance with invalid file name " + invalidFileName,
                Paper.Id.fromFileName(invalidFileName).isPresent());
    }
}
