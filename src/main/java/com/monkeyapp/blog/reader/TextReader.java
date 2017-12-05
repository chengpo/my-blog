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

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TextReader extends AbstractReader {
    // reader first 10 of lines
    private static final int PARTIAL_FILE_LINES = 10;

    private static final Function<String, Optional<String>> PARTIAL_READER_FUNC =
            (String path)-> {
                try {
                    return Optional.of(Files.lines(Paths.get(path))
                            .limit(PARTIAL_FILE_LINES)
                            .collect(Collectors.joining(System.lineSeparator())));
                } catch (IOException e) {
                    return Optional.empty();
                }

            };

    private static final Function<String, Optional<String>> COMPLETE_READER_FUNC =
            (String path)-> {
                try {
                    return Optional.of(new String(Files.readAllBytes(Paths.get(path))));
                } catch (IOException e) {
                    Logger.getLogger(TextReader.class).fatal("failed to read file: " + path);
                    Logger.getLogger(TextReader.class).fatal("IO exception: " + e.getMessage());
                    return Optional.empty();
                }
            };

    public static TextReader partialReader(String path) {
        return new TextReader(path, PARTIAL_READER_FUNC);
    }

    public static TextReader completeReader(String path) {
        return new TextReader(path, COMPLETE_READER_FUNC);
    }

    private final String path;
    private final Function<String, Optional<String>> readerFunc;

    private TextReader(String path, Function<String, Optional<String>> readerFunc) {
        this.path = path;
        this.readerFunc = readerFunc;
    }

    @Override
    public Optional<String> read() {
        return readerFunc.apply(path);
    }
}
