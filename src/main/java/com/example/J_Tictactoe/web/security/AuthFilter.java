package com.example.J_Tictactoe.web.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthFilter extends GenericFilterBean {
    private final JwtProvider jwtProvider;


    public AuthFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override 
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getServletPath();
        if (path.startsWith("/auth/") || path.equals("/") || path.equals("/favicon.ico") || 
            path.endsWith(".html") || path.endsWith(".js") || path.endsWith(".css")) {
            chain.doFilter(request, response);
            return;
        }
        
        String authHeader = httpRequest.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized: Missing or invalid Authorization header");
            return;
        }

        String token = authHeader.substring(7);
        if (!jwtProvider.validateAccessToken(token)) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized: Token is invalid or expired");
            return;
        }

        try {
            Claims claims = jwtProvider.getClaims(token);
            JwtAuthentication authentication = JwtUtil.createJwtAuthentication(claims);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            httpRequest.setAttribute("userId", UUID.fromString(claims.getSubject()));
            
            chain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized: " + e.getMessage());
        }
    }
}
