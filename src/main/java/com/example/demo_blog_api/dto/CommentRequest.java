package com.example.demo_blog_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentRequest(
        @NotNull(message = "User id is required")
        Long userId,

        @NotBlank(message = "Content is required")
        String content
) {
}
