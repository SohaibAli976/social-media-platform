package com.social_media_platform.configuration;

import com.social_media_platform.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.LogManager;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtTokenUtil;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(JwtUtil jwtTokenUtil
                                 //  AuthenticationManager authenticationManager
    ) {
        this.jwtTokenUtil = jwtTokenUtil;
        //this.authenticationManager = authenticationManager;
    }


    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Skip authentication for login and registration endpoints
        if (request.getRequestURI().startsWith("/auth/login") || request.getRequestURI().startsWith("/users/register")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Avoid re-authenticating if the user is already authenticated
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getJwtFromRequest(request);
        if (token != null && jwtTokenUtil.validateToken(token)) {
            String username = jwtTokenUtil.getUsername(token);

            // Add authentication to SecurityContext if the user is valid
            UserDetails userDetails = new User(username, "", new ArrayList<>());
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authentication successful for user: {}", username); // Add logging to track successful authentication
        } else {
            log.warn("Invalid JWT token or expired token detected for request: {}", request.getRequestURI()); // Log invalid token attempt
        }

        filterChain.doFilter(request, response);
    }

}

