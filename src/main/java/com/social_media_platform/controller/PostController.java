package com.social_media_platform.controller;

import com.social_media_platform.dto.request.SearchRequest;
import com.social_media_platform.entity.Comment;
import com.social_media_platform.entity.Post;
import com.social_media_platform.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody Post post, @RequestHeader("Authorization") String token) {
        return postService.createPost(post, token);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody Post post, @RequestHeader("Authorization") String token) {
        return postService.updatePost(id, post, token);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return postService.deletePost(id, token);
    }
    @GetMapping
    public ResponseEntity<?> getAllPosts(Pageable pageable) {
        return postService.getAllPosts(pageable);
    }
    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable Long id, @RequestBody Comment comment, @RequestHeader("Authorization") String token) {
        return postService.addComment(id, comment, token);
    }
    @PostMapping("/{id}/like")
    public ResponseEntity<?> likePost(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        return postService.likePost(id, token);
    }
    @PostMapping("/search")
    public ResponseEntity<?> searchPosts(@RequestBody SearchRequest searchRequest, Pageable pageable) {
        return postService.searchPosts(searchRequest, pageable);
    }

}
