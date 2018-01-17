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

package com.monkeyapp.blog;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.JarScanType;
import org.apache.tomcat.util.scan.StandardJarScanner;

import javax.servlet.ServletException;
import java.io.File;
import java.net.MalformedURLException;

public class App {
    static {
        // Turn off apache bean utils log
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog");
    }

    private static final int PORT = 8080;
    private static final String WEB_CONTENT = "src/main/webapp/";
    private static final String WEB_XML = (WEB_CONTENT + "WEB-INF/web.xml").replace("/", File.separator);

    public static void main( String[] args ) throws ServletException, LifecycleException, MalformedURLException {
        final Tomcat tomcat = new Tomcat();

        // Define and bind web.xml file location.
        final StandardContext context = (StandardContext)tomcat.addWebapp("/", new File(WEB_CONTENT).getAbsolutePath());
        context.setConfigFile(new File(WEB_XML).toURI().toURL());

        // Disable jar scanner
        context.setJarScanner(new StandardJarScanner() {
            {
                setJarScanFilter((JarScanType jarScanType, String jarName) -> false);
            }
        });

        if (args.length == 2 && args[0].equals("--port")) {
            tomcat.setPort(Integer.parseInt(args[1]));
        } else {
            tomcat.setPort(PORT);
        }

        // Enable compression response
        final Connector connector = tomcat.getConnector();
        connector.setProperty("compression", "on");
        connector.setProperty("compressionMinSize", "256");
        connector.setProperty("compressableMimeType", "text/html, text/xml, text/css, application/json, application/javascript");

        tomcat.start();
        tomcat.getServer().await();
    }
}
