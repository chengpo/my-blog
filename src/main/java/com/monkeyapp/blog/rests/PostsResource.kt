/*
MIT License

Copyright (c) 2017 - 2022 Po Cheng

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
package com.monkeyapp.blog.rests

import com.monkeyapp.blog.di.RootScope
import com.monkeyapp.blog.dtos.PostChunkDto
import com.monkeyapp.blog.dtos.PostDto

import javax.inject.Inject
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path("/posts")
class PostsResource {
    @Inject
    private lateinit var rootScope: RootScope

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getPostChunk(@DefaultValue("") @QueryParam("tag") tag: String,
                     @DefaultValue("0") @QueryParam("offset") offset: Long): PostChunkDto {
        return rootScope
            .visitorScope()
            .postController()
            .postChunk(tag, offset)
    }

    @GET
    @Path("/{year}/{monthday}/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getPostContent(@PathParam("year") year: String,
                       @PathParam("monthday") monthday: String,
                       @PathParam("title") title: String): PostDto {
        return rootScope
            .visitorScope()
            .postController()
            .postContent(year, monthday, title)
            .orElseThrow { WebApplicationException(404) }
    }
}
