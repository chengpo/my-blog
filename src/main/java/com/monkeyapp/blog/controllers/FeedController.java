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

package com.monkeyapp.blog.controllers;

import com.monkeyapp.blog.dtos.SyncFeedDto;
import com.monkeyapp.blog.dtos.TypeConverter;
import com.monkeyapp.blog.models.PaperRepository;
import com.monkeyapp.blog.models.SyncFeed;
import com.monkeyapp.blog.models.SyncFeedBuilder;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/feed")
public class FeedController {
    @Inject
    private PaperRepository paperRepository;

    @Inject
    private TypeConverter typeConverter;

    @GET
    @Produces("application/rss+xml")
    public SyncFeedDto getFeed() {
        final SyncFeed syncFeed = new SyncFeedBuilder(paperRepository).build();
        return typeConverter.toSyncFeedDto(syncFeed);
    }
}
