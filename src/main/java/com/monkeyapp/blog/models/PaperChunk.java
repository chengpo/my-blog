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

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PaperChunk {
    @JsonProperty("papers")
    private final List<Paper> papers;

    @JsonProperty("offset")
    private final int offset; // offset to the very first blog

    @JsonProperty("capacity")
    private final int capacity;

    @JsonProperty("eof")
    private final boolean eof; // eof = true when reach the last blog

    public PaperChunk(List<Paper> papers, int offset, int capacity, boolean eof) {
        this.papers = papers;
        this.offset = offset;
        this.capacity = capacity;
        this.eof = eof;
    }
}
