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
package com.monkeyapp.blog

import org.apache.catalina.LifecycleException
import org.apache.catalina.core.StandardContext
import org.apache.catalina.startup.Tomcat
import org.apache.tomcat.JarScanFilter
import org.apache.tomcat.util.scan.StandardJarScanner
import java.io.File
import java.net.MalformedURLException
import javax.servlet.ServletException

object App {
    @JvmStatic
    @Throws(ServletException::class, LifecycleException::class, MalformedURLException::class)
    fun main(args: Array<String>) {
        Tomcat().apply {
            setPort(args.port())
            updateContext()
            updateConnectorProperty()
        }.also { tomcat ->
            tomcat.start()
            tomcat.server.await()
        }
    }
}

private fun Array<String>.port(defaultPort: Int = 8080): Int {
    return if (size >= 2 && get(0) == "--port") get(1).toInt() else defaultPort
}

private fun Tomcat.updateContext() {
    val webContent = "src/main/webapp"
    val webXml = "${webContent}/WEB-INF/web.xml".replace("/", File.separator)

    (addWebapp("/", File(webContent).absolutePath) as StandardContext).apply {
        // Define and bind web.xml file location.
        configFile = File(webXml).toURI().toURL()
        // Disable jar scanner
        jarScanner = object : StandardJarScanner() {
            init {
                jarScanFilter = JarScanFilter { _, _ -> false }
            }
        }
    }
}

private fun Tomcat.updateConnectorProperty() {
    // Enable compression response
    listOf(
        "compression" to "on",
        "compressionMinSize"  to "256",
        "compressableMimeType" to  "text/html, text/xml, text/css, application/json, application/javascript"
    ).forEach { (k, v) -> connector.setProperty(k, v) }
}
