package com.example.demo_blog_api.service;

import com.example.demo_blog_api.dto.CategoryRequest;
import com.example.demo_blog_api.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> findAll();
    CategoryResponse findById(Long id);
    CategoryResponse save(CategoryRequest request);
    CategoryResponse update(CategoryRequest request, Long id);
    void delete(Long id);
    List<CategoryResponse> searchByName(String name);
}
