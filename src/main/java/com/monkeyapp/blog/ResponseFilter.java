package com.monkeyapp.blog;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResponseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        var httpRequest = (HttpServletRequest) request;
        var acceptHeader = httpRequest.getHeader("Accept");
        if (acceptHeader != null && acceptHeader.contains("text/html")) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.addHeader("Content-Security-Policy", "default-src 'self'; " +
                    "  connect-src 'self' https://www.googletagmanager.com https://www.google-analytics.com https://cdnjs.cloudflare.com;" +
                    "  script-src 'self' 'unsafe-inline' https://www.googletagmanager.com https://www.google-analytics.com https://cdnjs.cloudflare.com;" +
                    "  img-src 'self' data: https://www.googletagmanager.com https://www.google-analytics.com https://stats.g.doubleclick.net https://www.google.com; " +
                    "  style-src 'self' 'unsafe-inline' https://cdnjs.cloudflare.com https://fonts.googleapis.com;" +
                    "  font-src 'self' https://fonts.googleapis.com https://fonts.gstatic.com;" +
                    "  frame-ancestors 'none';");
            httpResponse.addHeader("Strict-Transport-Security", "max-age=63072000; includeSubDomains; preload");
            httpResponse.addHeader("X-Content-Type-Options", "nosniff");
            httpResponse.addHeader("X-Frame-Options", "DENY");
            httpResponse.addHeader("X-XSS-Protection", "1; mode=block");
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
