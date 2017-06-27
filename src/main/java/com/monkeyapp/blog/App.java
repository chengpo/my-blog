package com.monkeyapp.blog;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.JarScanType;
import org.apache.tomcat.util.scan.StandardJarScanner;

import javax.servlet.ServletException;
import java.io.File;
import java.net.MalformedURLException;

public class App {
    private static final int PORT = 8080;
    private static final String WEB_CONTENT = "src/main/webapp/";
    private static final String WEB_XML = WEB_CONTENT + "WEB-INF/web.xml";

    public static void main( String[] args ) throws ServletException, LifecycleException, MalformedURLException {
        final Tomcat tomcat = new Tomcat();

        // Define and bind web.xml file location.
        final Context context = tomcat.addWebapp("/", new File(WEB_CONTENT).getAbsolutePath());
        context.setConfigFile(new File(WEB_XML).toURI().toURL());

        // Disable jar scanner
        context.setJarScanner(new StandardJarScanner() {
            {
                setJarScanFilter((JarScanType jarScanType, String jarName) -> false);
            }
        });

        tomcat.setPort(PORT);
        tomcat.start();
        tomcat.getServer().await();
    }
}
