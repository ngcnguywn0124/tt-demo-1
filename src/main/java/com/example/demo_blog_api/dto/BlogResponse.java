package com.example.demo_blog_api.dto;

import com.example.demo_blog_api.entity.BlogStatus;

import java.time.LocalDateTime;
import java.util.Set;

public record BlogResponse(
        Long id,
        String title,
        String slug,
        String summary,
        String content,
        UserResponse author,
        CategoryResponse category,
        Set<TagResponse> tags,
        BlogStatus status,
        LocalDateTime publishedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
