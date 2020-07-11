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
package com.monkeyapp.blog

import org.apache.catalina.LifecycleException
import org.apache.catalina.core.StandardContext
import org.apache.catalina.startup.Tomcat
import org.apache.tomcat.JarScanFilter
import org.apache.tomcat.JarScanType
import org.apache.tomcat.util.scan.StandardJarScanner
import java.io.File
import java.net.MalformedURLException
import javax.servlet.ServletException

object App {
    private const val PORT = 8080
    private const val WEB_CONTENT = "src/main/webapp/"
    private val WEB_XML = (WEB_CONTENT + "WEB-INF/web.xml").replace("/", File.separator)

    @Throws(ServletException::class, LifecycleException::class, MalformedURLException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val tomcat = Tomcat()

        // Define and bind web.xml file location.
        val context = tomcat.addWebapp("/", File(WEB_CONTENT).absolutePath) as StandardContext
        context.configFile = File(WEB_XML).toURI().toURL()

        // Disable jar scanner
        context.jarScanner = object : StandardJarScanner() {
            init {
                jarScanFilter = JarScanFilter { jarScanType: JarScanType?, jarName: String? -> false }
            }
        }
        if (args.size == 2 && args[0] == "--port") {
            tomcat.setPort(args[1].toInt())
        } else {
            tomcat.setPort(PORT)
        }

        // Enable compression response
        val connector = tomcat.connector
        connector.setProperty("compression", "on")
        connector.setProperty("compressionMinSize", "256")
        connector.setProperty("compressableMimeType", "text/html, text/xml, text/css, application/json, application/javascript")
        tomcat.start()
        tomcat.server.await()
    }

    init {
        // Turn off apache bean utils log
        System.setProperty("org.apache.commons.logging.Log",
                "org.apache.commons.logging.impl.NoOpLog")
    }
}