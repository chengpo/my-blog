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

package com.monkeyapp.blog.controllers;

import com.monkeyapp.blog.dtos.PaperChunkDto;
import com.monkeyapp.blog.dtos.PaperDto;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class PostsControllerTest extends BaseControllerTest {
    @Test
    public void testGetPostChunk() {
        Response response = target("/posts").request().get();
        assertThat(response.getStatus(), is(200));

        PaperChunkDto paperChunk = response.readEntity(PaperChunkDto.class);
        assertNotNull(paperChunk);
        assertNotNull(paperChunk.getPapers());
        assertThat(paperChunk.getPapers().size(), is(1));
    }

    @Test
    public void testGetValidPost() {
        Response response = target("/posts/2017/0521/mock-post").request().get();
        assertThat(response.getStatus(), is(200));

        PaperDto paperDto = response.readEntity(PaperDto.class);
        assertNotNull(paperDto);
        assertThat(paperDto.getContent(), is("<p>mock post content</p>\n"));
        assertNotNull(paperDto.getFile());
        assertThat(paperDto.getFile().getCreationTime(), is("2017/05/21 01:48"));
        assertThat(paperDto.getFile().getTag(), is("Tag"));
        assertThat(paperDto.getFile().getTitle(), is("Mock Post"));
        assertThat(paperDto.getFile().getUrl(), is("2017/0521/mock-post"));
    }
}
