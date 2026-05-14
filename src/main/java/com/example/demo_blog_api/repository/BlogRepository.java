package com.example.demo_blog_api.repository;

import com.example.demo_blog_api.entity.Blog;
import com.example.demo_blog_api.entity.BlogStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    boolean existsBySlug(String slug);

    @EntityGraph(attributePaths = {"author", "author.role", "category", "tags"})
    @Query("select distinct b from Blog b order by b.createdAt desc")
    List<Blog> findAllWithRelations();

    @EntityGraph(attributePaths = {"author", "author.role", "category", "tags"})
    @Query("select distinct b from Blog b where b.status = :status order by b.publishedAt desc, b.createdAt desc")
    List<Blog> findAllByStatusWithRelations(@Param("status") BlogStatus status);

    @EntityGraph(attributePaths = {"author", "author.role", "category", "tags"})
    @Query("select distinct b from Blog b where b.id = :id")
    Optional<Blog> findByIdWithRelations(@Param("id") Long id);

    @EntityGraph(attributePaths = {"author", "author.role", "category", "tags"})
    Optional<Blog> findBySlug(String slug);
}
