package com.example.demo_blog_api.repository;

import com.example.demo_blog_api.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}
