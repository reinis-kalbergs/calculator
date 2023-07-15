package com.example.calculator.configuration.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiKeyFilter implements Filter {

    private static final String API_KEY_HEADER = "ApiKey";
    private static final String API_KEY_VALUE = "8c066128-ac81-4f7a-a956-1f9930bf477e";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse res = (HttpServletResponse) servletResponse;

        String apiKeyHeader = req.getHeader(API_KEY_HEADER);
        if (apiKeyHeader == null || !apiKeyHeader.equals(API_KEY_VALUE)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid API Key");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}

