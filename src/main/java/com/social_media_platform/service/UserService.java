package com.social_media_platform.service;

import com.social_media_platform.dto.request.LoginRequest;
import com.social_media_platform.dto.request.SearchRequest;
import com.social_media_platform.dto.request.UserRegisterRequest;
import com.social_media_platform.dto.response.JwtResponse;
import com.social_media_platform.entity.Follow;
import com.social_media_platform.entity.User;
import com.social_media_platform.repository.FollowRepository;
import com.social_media_platform.repository.UserRepository;
import com.social_media_platform.util.JwtUtil;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@NoArgsConstructor(force = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FollowRepository followRepository;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       FollowRepository followRepository,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.followRepository = followRepository;
        this.jwtUtil = jwtUtil;
    }

    // Register a new user
    public ResponseEntity<?> registerUser(UserRegisterRequest userRegisterRequest) {
        // Check if the email already exists
        assert userRepository != null;
        if (userRepository.existsByEmail(userRegisterRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use");
        }

        // Create a new User entity from the request DTO
        User user = new User();
        user.setUsername(userRegisterRequest.getUsername());
        user.setEmail(userRegisterRequest.getEmail());
        assert passwordEncoder != null;
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword())); // Encode password
        user.setBio(userRegisterRequest.getBio());
        user.setProfilePicture(userRegisterRequest.getProfilePicture());

        // Save the user to the repository
        userRepository.save(user);

        // Return the created user, excluding sensitive information
        user.setPassword(null); // Ensure the password is not returned in the response
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    public ResponseEntity<?> loginUser(LoginRequest loginRequest) {
        assert userRepository != null;
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.isEmpty() || !Objects.requireNonNull(passwordEncoder).matches(loginRequest.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        assert jwtUtil != null;
        String token = jwtUtil.generateToken(user.get().getUsername());
        return ResponseEntity.ok(new JwtResponse(token, user.get().getId(), user.get().getUsername(), user.get().getEmail()));
    }
    public ResponseEntity<?> getUserProfile(Long id) {
        assert userRepository != null;
        Optional<User> user = userRepository.findById(id);

         if(user.isPresent())
              return  ResponseEntity.ok(user.get());
        else return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    public ResponseEntity<?> followUser(Long id, String token) {
        assert userRepository != null;
        Optional<User> userToFollow = userRepository.findById(id);
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> currentUser = userRepository.findByUsername(username);

        if (userToFollow.isEmpty() || currentUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        Follow follow = new Follow();
        follow.setFollower(currentUser.get());
        follow.setFollowing(userToFollow.get());
        assert followRepository != null;
        followRepository.save(follow);
        return ResponseEntity.ok("Followed successfully");
    }
    public ResponseEntity<?> getFollowers(Long id) {
        assert userRepository != null;
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        Set<User> followers = user.get().getFollowers();
        return ResponseEntity.ok(followers);
    }

    public ResponseEntity<?> getFollowing(Long id) {
        assert userRepository != null;
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        Set<User> following = user.get().getFollowers();
        return ResponseEntity.ok(following);
    }
    public ResponseEntity<?> searchUsers(SearchRequest searchRequest, Pageable pageable) {
        assert userRepository != null;
        Page<User> users = userRepository.findByUsernameContainingOrEmailContainingOrBioContaining(searchRequest.getKeyword(), searchRequest.getKeyword(), searchRequest.getKeyword(), pageable);
        return ResponseEntity.ok(users);
    }



}
