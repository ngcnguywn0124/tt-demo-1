package com.example.demo_blog_api.dto;

import com.example.demo_blog_api.entity.BlogStatus;
import jakarta.validation.constraints.NotNull;

public record BlogStatusRequest(
        @NotNull(message = "Status is required")
        BlogStatus status
) {
}
