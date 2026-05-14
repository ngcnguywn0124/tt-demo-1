package com.example.demo_blog_api.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(
        @NotBlank(message = "Content is required")
        String content
) {
}
