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

import com.monkeyapp.blog.FileWrapperImpl;
import com.monkeyapp.blog.models.PaperRepository;
import com.monkeyapp.blog.wrappers.FileWrapper;
import com.monkeyapp.blog.wrappers.StorageWrapper;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.monitoring.MonitoringStatistics;

import javax.inject.Provider;
import javax.inject.Singleton;
import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class MockBinder extends AbstractBinder {
    @Override
    protected void configure() {
        // mock storage
        final List<String> pageFiles = Collections.singletonList("2017-0509-0011-tag-mock-page.md");
        final FileWrapper pageFile = spy(new FileWrapperImpl("pages/2017-0509-0011-tag-mock-page.md"));
        doReturn(Optional.of("mock page content")).when(pageFile).completeRead();

        final List<String> postFiles = Collections.singletonList("2017-0521-0148-tag-mock-post.md");
        final FileWrapper postFile = spy(new FileWrapperImpl("posts/2017-0521-0148-tag-mock-post.md"));
        doReturn(Optional.of("mock post content")).when(postFile).completeRead();
        doReturn(Optional.of("mock post content")).when(postFile).partialRead();

        final StorageWrapper storageWrapper = mock(StorageWrapper.class);

        doReturn(pageFiles).when(storageWrapper).listPageFiles();
        doReturn(postFiles).when(storageWrapper).listPostFiles();
        doReturn(pageFile).when(storageWrapper).openPageFile(anyString());
        doReturn(postFile).when(storageWrapper).openPostFile(anyString());

        bindFactory(()->new PaperRepository(storageWrapper)).to(PaperRepository.class).in(Singleton.class);

        // mock servlet context
        ServletContext servletContext = mock(ServletContext.class);
        doReturn("10").when(servletContext).getInitParameter(eq("post-per-chunk"));

        bindFactory(() -> servletContext).to(ServletContext.class);
    }
}
