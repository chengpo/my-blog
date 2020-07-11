/*
MIT License

Copyright (c) 2017 - 2018 Po Cheng

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

import com.monkeyapp.blog.wrappers.FileWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class PaperFileTest {
    @Mock
    private FileWrapper fileWrapper;

    @Before
    public void setUp() {
        doReturn("2017").when(fileWrapper).year;
        doReturn("0418").when(fileWrapper).monthday;
        doReturn("1053").when(fileWrapper).time;
        doReturn("tag").when(fileWrapper).tag;
        doReturn("arbitrary-post-title").when(fileWrapper).title;
    }

    @Test
    public void testGetValidPaperFileProperties() {
        PaperFile file = new PaperFile(fileWrapper);
        assertThat(file.getTitle(), is("Arbitrary Post Title"));
        assertThat(file.getTag(), is("Tag") );
        assertThat(file.getUrl(), is("2017/0418/arbitrary-post-title"));
    }
}
