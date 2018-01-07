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
        doReturn("2017").when(fileWrapper).getYear();
        doReturn("0418").when(fileWrapper).getMonthday();
        doReturn("1053").when(fileWrapper).getTime();
        doReturn("tag").when(fileWrapper).getTag();
        doReturn("arbitrary-post-title").when(fileWrapper).getTitle();
    }

    @Test
    public void testGetValidPaperFileProperties() {
        PaperFile file = new PaperFile(fileWrapper);
        assertThat(file.getTitle(), is("Arbitrary Post Title"));
        assertThat(file.getTag(), is("Tag") );
        assertThat(file.getUrl(), is("2017/0418/arbitrary-post-title"));
    }
}
