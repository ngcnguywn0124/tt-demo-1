package com.example.demo_blog_api.service;

import com.example.demo_blog_api.dto.BlogRequest;
import com.example.demo_blog_api.dto.BlogResponse;
import com.example.demo_blog_api.entity.BlogStatus;

import java.util.List;

public interface BlogService {
    List<BlogResponse> getPublishedBlogs();
    List<BlogResponse> getAllBlogs();
    List<BlogResponse> getMyBlogs(String username);
    BlogResponse getPublishedById(Long id);
    BlogResponse getPublishedBySlug(String slug);
    BlogResponse create(BlogRequest request, String username);
    BlogResponse update(Long id, BlogRequest request, String username, boolean admin);
    BlogResponse updateStatus(Long id, BlogStatus status);
    void delete(Long id, String username, boolean admin);
}
