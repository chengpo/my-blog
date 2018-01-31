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

import com.monkeyapp.blog.FileWrapperImpl;
import com.monkeyapp.blog.wrappers.FileWrapper;
import com.monkeyapp.blog.wrappers.StorageWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class SyncFeedBuilderTest {
    @Mock
    private StorageWrapper storageWrapper;

    private PaperRepository paperRepository;

    @Before
    public void setUp() {
        doReturn(
                Arrays.asList(
                        "2017-0418-1053-tag2-post3.md",
                        "2017-0418-1153-tag2-post2.md",
                        "2018-0418-1205-tag1-post1.md"
                )).when(storageWrapper).listPostFiles();

        doAnswer(invocationOnMock -> {
            String fileName = (String) invocationOnMock.getArguments()[0];
            FileWrapper fileWrapper = spy(new FileWrapperImpl(fileName));
            doReturn(Optional.of("mock partial post content")).when(fileWrapper).partialRead();
            return fileWrapper;
        }).when(storageWrapper).openPostFile(anyString());

        paperRepository = new PaperRepository(storageWrapper);
    }

    @Test
    public void testBuildFeed() {
        SyncFeed syncFeed = new SyncFeedBuilder(paperRepository).build();
        assertThat(syncFeed.getChannel().getItems().size(), is(3));
    }
}
