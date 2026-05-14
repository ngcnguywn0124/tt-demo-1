package com.example.demo_blog_api.controller;

import com.example.demo_blog_api.dto.CommentRequest;
import com.example.demo_blog_api.dto.CommentResponse;
import com.example.demo_blog_api.dto.CommentStatusRequest;
import com.example.demo_blog_api.dto.CommentUpdateRequest;
import com.example.demo_blog_api.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/blogs/{blogId}/comments")
    public ResponseEntity<List<CommentResponse>> getVisibleByBlog(@PathVariable Long blogId) {
        return ResponseEntity.ok(commentService.getVisibleByBlog(blogId));
    }

    @GetMapping("/blogs/{blogId}/comments/admin")
    public ResponseEntity<List<CommentResponse>> getAllByBlog(@PathVariable Long blogId) {
        return ResponseEntity.ok(commentService.getAllByBlog(blogId));
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<CommentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getById(id));
    }

    @PostMapping("/blogs/{blogId}/comments")
    public ResponseEntity<CommentResponse> create(
            @PathVariable Long blogId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(blogId, request, authentication.getName()));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(commentService.update(id, request.content(), authentication.getName(), isAdmin(authentication)));
    }

    @PatchMapping("/comments/{id}/status")
    public ResponseEntity<CommentResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody CommentStatusRequest request
    ) {
        return ResponseEntity.ok(commentService.updateStatus(id, request.status()));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        commentService.delete(id, authentication.getName(), isAdmin(authentication));
        return ResponseEntity.noContent().build();
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
