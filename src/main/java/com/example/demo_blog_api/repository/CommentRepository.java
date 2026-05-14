package com.example.demo_blog_api.repository;

import com.example.demo_blog_api.entity.Comment;
import com.example.demo_blog_api.entity.CommentStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"user", "user.role", "blog"})
    List<Comment> findByBlogIdAndStatusOrderByCreatedAtDesc(Long blogId, CommentStatus status);

    @EntityGraph(attributePaths = {"user", "user.role", "blog"})
    List<Comment> findByBlogIdOrderByCreatedAtDesc(Long blogId);

    @EntityGraph(attributePaths = {"user", "user.role", "blog"})
    @Query("select c from Comment c where c.id = :id")
    Optional<Comment> findByIdWithRelations(@Param("id") Long id);
}
