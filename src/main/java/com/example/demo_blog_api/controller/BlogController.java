package com.example.demo_blog_api.controller;

import com.example.demo_blog_api.dto.BlogRequest;
import com.example.demo_blog_api.dto.BlogResponse;
import com.example.demo_blog_api.dto.BlogStatusRequest;
import com.example.demo_blog_api.service.BlogService;
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
@RequestMapping("/blogs")
public class BlogController {
    private final BlogService blogService;

    @GetMapping
    public ResponseEntity<List<BlogResponse>> getPublishedBlogs() {
        return ResponseEntity.ok(blogService.getPublishedBlogs());
    }

    @GetMapping("/admin")
    public ResponseEntity<List<BlogResponse>> getAllBlogs() {
        return ResponseEntity.ok(blogService.getAllBlogs());
    }

    @GetMapping("/me")
    public ResponseEntity<List<BlogResponse>> getMyBlogs(Authentication authentication) {
        return ResponseEntity.ok(blogService.getMyBlogs(authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(blogService.getPublishedById(id));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<BlogResponse> getBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(blogService.getPublishedBySlug(slug));
    }

    @PostMapping
    public ResponseEntity<BlogResponse> create(@Valid @RequestBody BlogRequest request, Authentication authentication) {
        return ResponseEntity.status(HttpStatus.CREATED).body(blogService.create(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody BlogRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(blogService.update(id, request, authentication.getName(), isAdmin(authentication)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BlogResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody BlogStatusRequest request
    ) {
        return ResponseEntity.ok(blogService.updateStatus(id, request.status()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Authentication authentication) {
        blogService.delete(id, authentication.getName(), isAdmin(authentication));
        return ResponseEntity.noContent().build();
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}
