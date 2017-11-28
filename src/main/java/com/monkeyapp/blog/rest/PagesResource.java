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

package com.monkeyapp.blog.rest;

import com.monkeyapp.blog.module.Entity;
import com.monkeyapp.blog.reader.MarkdownReader;
import com.monkeyapp.blog.module.Post;
import com.monkeyapp.blog.reader.TextReader;

import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.util.Optional;

@Path("/site-pages")
public class PagesResource {
    @Context
    private ServletContext context;

    @GET @Path("/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Post getSitePage(@PathParam("name") String name) {
        return Optional.ofNullable(Entity.fromFileName(name))
                .map((entity) -> {
                    final String path = context.getRealPath("/") + ("md/pages/" + entity.getName()).replace("/", File.separator);
                    return new Post(entity, new MarkdownReader(TextReader.fullReader(path)).read());
                })
                .orElseThrow(() -> new WebApplicationException(404));
    }
}
