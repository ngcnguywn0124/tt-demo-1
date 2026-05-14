package com.example.demo_blog_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo_blog_api.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlug(String slug);
    List<Category> findByNameContainingIgnoreCase(String name);
    Optional<Category> findBySlug(String slug);
}
