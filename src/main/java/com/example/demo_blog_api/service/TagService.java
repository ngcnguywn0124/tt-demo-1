package com.example.demo_blog_api.service;

import com.example.demo_blog_api.dto.TagRequest;
import com.example.demo_blog_api.dto.TagResponse;

import java.util.List;

public interface TagService {
    List<TagResponse> getAll();
    TagResponse getById(Long id);
    TagResponse add(TagRequest request);
    TagResponse update(TagRequest request, Long id);
    void delete(Long id);
    List<TagResponse> searchByName(String name);
}
