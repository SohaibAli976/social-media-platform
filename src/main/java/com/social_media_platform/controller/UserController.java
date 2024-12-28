package com.social_media_platform.controller;

import com.social_media_platform.dto.request.LoginRequest;
import com.social_media_platform.dto.request.SearchRequest;
import com.social_media_platform.dto.request.UserRegisterRequest;
import com.social_media_platform.entity.User;
import com.social_media_platform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterRequest user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        return userService.loginUser(loginRequest);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserProfile(id);
    }
    @PostMapping("/{id}/follow")
    public ResponseEntity<?> followUser(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return userService.followUser(id, token);
    }
    @GetMapping("/{id}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable Long id) {
        return userService.getFollowers(id);
    }
    @GetMapping("/{id}/following")
    public ResponseEntity<?> getFollowing(@PathVariable Long id) {
        return userService.getFollowing(id);
    }
    @PostMapping("/search")
    public ResponseEntity<?> searchUsers(@RequestBody SearchRequest searchRequest, Pageable pageable) {
        return userService.searchUsers(searchRequest, pageable);
    }


}
