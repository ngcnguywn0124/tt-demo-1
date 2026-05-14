package com.example.demo_blog_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo_blog_api.entity.Category;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
//    @Query("select a from Category a where a.name like :name")
//    List<Category> findByNameLike(@Param("name") String name);
    Optional<Category> findByNameLike(String name);
}
