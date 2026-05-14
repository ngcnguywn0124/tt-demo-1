package com.example.demo_blog_api.service;

import com.example.demo_blog_api.dto.CommentRequest;
import com.example.demo_blog_api.dto.CommentResponse;
import com.example.demo_blog_api.entity.CommentStatus;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getVisibleByBlog(Long blogId);
    List<CommentResponse> getAllByBlog(Long blogId);
    CommentResponse getById(Long id);
    CommentResponse create(Long blogId, CommentRequest request, String username);
    CommentResponse update(Long id, String content, String username, boolean admin);
    CommentResponse updateStatus(Long id, CommentStatus status);
    void delete(Long id, String username, boolean admin);
}
