package com.example.demo_blog_api.service;

import com.example.demo_blog_api.dto.BlogRequest;
import com.example.demo_blog_api.dto.BlogResponse;
import com.example.demo_blog_api.entity.BlogStatus;

import java.util.List;

public interface BlogService {
    List<BlogResponse> getPublishedBlogs();
    List<BlogResponse> getAllBlogs();
    BlogResponse getById(Long id);
    BlogResponse getBySlug(String slug);
    BlogResponse create(BlogRequest request);
    BlogResponse update(Long id, BlogRequest request);
    BlogResponse updateStatus(Long id, BlogStatus status);
    void delete(Long id);
}
