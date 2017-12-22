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

package com.monkeyapp.blog.adapters;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TextReader {
    private static final int PARTIAL_FILE_LINES = 10;

    private static final Function<String, Optional<String>> PARTIAL_READ_FUNC =
            path -> {
                try (Stream<String> lines = Files.lines(Paths.get(path))) {
                    return Optional.of(lines.limit(PARTIAL_FILE_LINES)
                            .collect(Collectors.joining(System.lineSeparator())));
                } catch (IOException e) {
                    return Optional.empty();
                }
            };

    private static final Function<String, Optional<String>> COMPLETE_READ_FUNC =
            path -> {
                try {
                    return Optional.of(new String(Files.readAllBytes(Paths.get(path))));
                } catch (IOException e) {
                    Logger.getLogger(TextReader.class).fatal("failed to read file: " + path);
                    Logger.getLogger(TextReader.class).fatal("IO exception: " + e.getMessage());
                    return Optional.empty();
                }
            };

    static Optional<String> partialRead(String path) {
        return PARTIAL_READ_FUNC.apply(path);
    }

    static Optional<String> completeRead(String path) {
        return COMPLETE_READ_FUNC.apply(path);
    }
}
