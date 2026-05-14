package com.example.demo_blog_api.dto;

import com.example.demo_blog_api.entity.CommentStatus;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long blogId,
        String blogTitle,
        UserResponse user,
        String content,
        CommentStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
