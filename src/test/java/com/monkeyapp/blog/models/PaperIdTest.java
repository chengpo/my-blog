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

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class PaperIdTest {
    /*
    @Test
    public void testCreateIdInstanceWithValidFileName() {
        final String validFilename = "2017-0418-1053-tag-arbitrary-post-title.md";

        PaperId id = PaperId.fromFileName(validFilename).orElseThrow(() -> new AssertionFailedError("failed to path file name"));

        assertNotNull("failed to recreate paper id with valid file name " + validFilename, id);

        assertEquals("wrong post tag", "Tag", id.getTag());
        assertEquals("wrong post name", "Arbitrary Post Title", id.getTitle());
        assertEquals("wrong post priority", 201704181053L, id.getPriority());

        assertEquals("wrong post file name", validFilename, id.getName());
    }

    @Test
    public void testCreateIdInstanceWithInvalidFileName() {
        final String invalidFileName = "201704181053-tag-arbitrary-post-title.md";
        assertFalse("should not create paper id instance with invalid file name " + invalidFileName,
                PaperId.fromFileName(invalidFileName).isPresent());
    }
    */
}
