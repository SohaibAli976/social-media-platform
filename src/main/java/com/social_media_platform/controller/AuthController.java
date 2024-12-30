package com.social_media_platform.controller;

import com.social_media_platform.dto.request.LoginRequest;
import com.social_media_platform.entity.testUser;
import com.social_media_platform.util.JwtUtil;
import com.social_media_platform.service.UserService; // Assumed service to authenticate users
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;
    //private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService; // Service to authenticate users

    public AuthController( JwtUtil jwtUtil, UserService userService) {
        //this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
                return userService.loginUser(loginRequest);
        } catch (Exception e) {
            // In case of authentication failure, return a 401 Unauthorized response
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }
}

