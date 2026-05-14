package com.example.demo_blog_api.dto;

public record TagResponse(
        Long id,
        String name,
        String slug
) {
}
