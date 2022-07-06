package com.monkeyapp.blog;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (!httpRequest.getServletPath().endsWith(".js") && !httpRequest.getServletPath().endsWith(".css")) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.addHeader("Content-Security-Policy", "default-src 'self'; " +
                    "  script-src 'self' 'unsafe-inline' https://www.googletagmanager.com https://www.google-analytics.com https://cdnjs.cloudflare.com;" +
                    "  img-src 'self' data: https://www.google-analytics.com https://stats.g.doubleclick.net https://www.google.com; " +
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
