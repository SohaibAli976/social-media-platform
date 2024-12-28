package com.social_media_platform.service;

import com.social_media_platform.dto.request.SearchRequest;
import com.social_media_platform.entity.Comment;
import com.social_media_platform.entity.Post;
import com.social_media_platform.entity.User;
import com.social_media_platform.repository.CommentRepository;
import com.social_media_platform.repository.PostRepository;
import com.social_media_platform.repository.UserRepository;
import com.social_media_platform.util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;
//private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final JwtUtil jwtUtil;

    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       CommentRepository commentRepository, JwtUtil jwtUtil) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> createPost(Post post, String token) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        post.setUser(user.get());
        postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }
    public ResponseEntity<?> getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
        return ResponseEntity.ok(posts);
    }
    public ResponseEntity<?> getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);

        if (post.isPresent()) {
            return ResponseEntity.ok(post.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
    }
    public ResponseEntity<?> updatePost(Long id, Post post, String token) {
        Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!existingPost.get().getUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only edit your own post");
        }

        post.setId(id);
        post.setUser(existingPost.get().getUser());
        postRepository.save(post);
        return ResponseEntity.ok(post);
    }
    public ResponseEntity<?> deletePost(Long id, String token) {
        Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();;
        if (!existingPost.get().getUser().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own post");
        }

        postRepository.delete(existingPost.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    public ResponseEntity<?> addComment(Long id, Comment comment, String token) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        comment.setPost(post.get());
        comment.setUser(user.get());
        comment.setTimestamp(LocalDateTime.now());
        commentRepository.save(comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }
    public ResponseEntity<?> likePost(Long id, String token) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        if (post.get().getLikes().contains(user.get())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You already liked this post");
        }

        post.get().getLikes().add(user.get());
        postRepository.save(post.get());
        return ResponseEntity.ok(post.get());
    }
    public ResponseEntity<?> searchPosts(SearchRequest searchRequest, Pageable pageable) {
        Page<Post> posts = postRepository.findByContentContainingOrTitleContaining(searchRequest.getKeyword(), searchRequest.getKeyword(), pageable);
        return ResponseEntity.ok(posts);
    }


}
