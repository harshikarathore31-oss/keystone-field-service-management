package com.keystone.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        System.out.println("===== JWT FILTER =====");
        System.out.println(
                "Request: "
                        + request.getMethod()
                        + " "
                        + request.getRequestURI()
        );

        System.out.println(
                "Authorization header exists: "
                        + (authHeader != null)
        );

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            System.out.println("Bearer token received: YES");

            if (jwtService.validateToken(token)) {

                System.out.println("JWT validation: SUCCESS");

                String email = jwtService.extractEmail(token);

                System.out.println("JWT email: " + email);

                UserDetails userDetails =
                        customUserDetailsService
                                .loadUserByUsername(email);

                System.out.println(
                        "User loaded: "
                                + userDetails.getUsername()
                );

                System.out.println(
                        "Authorities: "
                                + userDetails.getAuthorities()
                );

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);

                System.out.println(
                        "Authentication set: "
                                + SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                );

            } else {

                System.out.println("JWT validation: FAILED");
            }

        } else {

            System.out.println("Bearer token received: NO");
        }

        System.out.println(
                "===== BEFORE SECURITY AUTHORIZATION ====="
        );

        var authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        System.out.println(
                "Authentication: "
                        + authentication
        );

        if (authentication != null) {

            System.out.println(
                    "Principal: "
                            + authentication.getPrincipal()
            );

            System.out.println(
                    "Authenticated: "
                            + authentication.isAuthenticated()
            );

            System.out.println(
                    "Authorities: "
                            + authentication.getAuthorities()
            );
        }

        System.out.println(
                "========================================"
        );

        // IMPORTANT:
        // Continue through the filter chain ONLY ONCE.
        filterChain.doFilter(request, response);
    }
}