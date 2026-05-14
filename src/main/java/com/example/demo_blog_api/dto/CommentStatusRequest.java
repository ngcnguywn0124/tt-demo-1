package com.example.demo_blog_api.dto;

import com.example.demo_blog_api.entity.CommentStatus;
import jakarta.validation.constraints.NotNull;

public record CommentStatusRequest(
        @NotNull(message = "Status is required")
        CommentStatus status
) {
}
