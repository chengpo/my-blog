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

package com.monkeyapp.blog;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monkeyapp.blog.models.PostRepository;
import jersey.repackaged.com.google.common.collect.ImmutableList;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.*;
import java.util.Collections;
import java.util.List;

public class AppContext implements ServletContextListener {
    private final Logger logger = Logger.getLogger(AppContext.class);

    private static String contextRoot;

    private static PostRepository postRepository;

    public static PostRepository getPostRepository() {
        return postRepository;
    }

    public static String getRealPostPath(String fileName) {
        return contextRoot + ("md/posts/" + fileName).replace("/", File.separator);
    }

    public static String getRealPagePath(String fileName) {
        return contextRoot + ("md/pages/" + fileName).replace("/", File.separator);
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final ServletContext context = sce.getServletContext();

        contextRoot = context.getRealPath("/");
        String fileListJsonPath = contextRoot + context.getInitParameter("file-list").replace("/", File.separator);

        logger.info("servlet context root = " + contextRoot);
        logger.info("file-list.json path = " + fileListJsonPath);

        // init post repository
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream(
                                new File(fileListJsonPath))))) {
            List<String> postFileNames = new ObjectMapper().readValue(reader, new TypeReference<List<String>>() {});
            postRepository = new PostRepository(ImmutableList.copyOf(postFileNames));
        } catch (IOException e) {
            logger.error("failed to load file-list.json. Error: " + e.getMessage());
            postRepository = new PostRepository(Collections.emptyList());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
