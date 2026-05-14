package com.example.demo_blog_api.repository;

import com.example.demo_blog_api.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsBySlug(String slug);
    List<Tag> findByNameContainingIgnoreCase(String name);
    Optional<Tag> findBySlug(String slug);
}
