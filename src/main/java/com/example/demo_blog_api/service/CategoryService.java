package com.example.demo_blog_api.service;

import com.example.demo_blog_api.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> findAll();
    Optional<Category> findById(Long id);
    Category save(Category category);
    Category update(Category category, Long id);
    void delete(Long id);
    Optional<Category> findByName(String name);
}
