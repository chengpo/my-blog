package com.monkeyapp.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ResponseFilterTest {
    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Mock
    private FilterChain filterChain;

    @Test
    public void testAddSecuirtyHeaders() throws IOException, ServletException{
        doReturn("").when(httpServletRequest).getServletPath();

        new ResponseFilter().doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse, times(1)).addHeader(eq("Content-Security-Policy"), anyString());
        verify(httpServletResponse, times(1)).addHeader(eq("Strict-Transport-Security"), anyString());
        verify(httpServletResponse, times(1)).addHeader(eq("X-Content-Type-Options"), anyString());
        verify(httpServletResponse, times(1)).addHeader(eq("X-Frame-Options"), anyString());
        verify(httpServletResponse, times(1)).addHeader(eq("X-XSS-Protection"), anyString());
        verify(filterChain, times(1)).doFilter(eq(httpServletRequest), eq(httpServletResponse));
    }

    @Test
    public void testNotAddSecuirtyHeadersForJsFiles() throws IOException, ServletException{
        doReturn("x.js").when(httpServletRequest).getServletPath();

        new ResponseFilter().doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse, never()).addHeader(anyString(), anyString());
        verify(filterChain, times(1)).doFilter(eq(httpServletRequest), eq(httpServletResponse));
    }

    @Test
    public void testNotAddSecuirtyHeadersForCssFiles() throws IOException, ServletException{
        doReturn("x.css").when(httpServletRequest).getServletPath();

        new ResponseFilter().doFilter(httpServletRequest, httpServletResponse, filterChain);

        verify(httpServletResponse, never()).addHeader(anyString(), anyString());
        verify(filterChain, times(1)).doFilter(eq(httpServletRequest), eq(httpServletResponse));
    }
}
