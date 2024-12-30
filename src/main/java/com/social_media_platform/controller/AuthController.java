package com.social_media_platform.controller;

import com.social_media_platform.dto.request.LoginRequest;
import com.social_media_platform.util.JwtUtil;
import com.social_media_platform.service.UserService; // Assumed service to authenticate users
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService; // Service to authenticate users

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        // Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate the JWT token
        String jwt = jwtUtil.generateToken(String.valueOf(authentication));

        // Return the token in the response
        return ResponseEntity.ok(jwt);
    }
}

