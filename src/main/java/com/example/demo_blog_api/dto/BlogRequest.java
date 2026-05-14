package com.example.demo_blog_api.dto;

import com.example.demo_blog_api.entity.BlogStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record BlogRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @NotBlank(message = "Summary is required")
        @Size(max = 500, message = "Summary must not exceed 500 characters")
        String summary,

        @NotBlank(message = "Content is required")
        String content,

        @NotNull(message = "Author id is required")
        Long authorId,

        @NotNull(message = "Category id is required")
        Long categoryId,

        Set<Long> tagIds,
        BlogStatus status
) {
}
