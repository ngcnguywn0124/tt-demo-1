package com.example.demo_blog_api.dto;

public record CategoryResponse(
        Long id,
        String name,
        String slug,
        String description
) {
}
