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

package com.monkeyapp.blog;

import com.monkeyapp.blog.wrappers.FileWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class FileWrapperImplTest {
    @Test
    public void testCreatedByPaperFileName() {
        final String paperFilename = "2017-0418-1053-tag-arbitrary-post-title.md";

        FileWrapper file = new FileWrapperImpl("post/" + paperFilename);

        assertThat(file.getName(), is("2017-0418-1053-tag-arbitrary-post-title.md"));
        assertThat(file.getTag(), is("tag"));
        assertThat(file.getYear(), is("2017"));
        assertThat(file.getMonthday(), is("0418"));
        assertThat(file.getTime(), is("1053"));
        assertThat(file.getTitle(), is("arbitrary-post-title"));
        assertThat(file.getPriority(), is(201704181053L));
        assertTrue(file.isPaper());
        assertThat(file.getName(), is(paperFilename));
    }

    @Test
    public void testCreateIdInstanceWithInvalidFileName() {
        final String jsonFileName = "file-list.json";

        FileWrapper file = new FileWrapperImpl("post/" + jsonFileName);
        assertFalse(file.isPaper());
    }
}
