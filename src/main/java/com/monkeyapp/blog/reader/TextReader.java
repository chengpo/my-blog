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

package com.monkeyapp.blog.reader;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Collectors;

public class TextReader extends AbstractReader {
    private final int maxLines;
    private final String path;

    public static TextReader partialReader(String path) {
        return new TextReader(path, 10);
    }

    public static TextReader fullReader(String path) {
        return new TextReader(path, -1);
    }

    private TextReader(String path, int maxLines) {
        this.path = path;
        this.maxLines = maxLines;
    }

    @Override
    public String read() {
        try {
            return (maxLines <= 0) ?
                    new String(Files.readAllBytes(Paths.get(path))):
                    Files.lines(Paths.get(path))
                            .limit(maxLines)
                            .collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new WebApplicationException(e);
        }
    }
}
