package com.example.demo_blog_api.dto;

import com.example.demo_blog_api.entity.Blog;
import com.example.demo_blog_api.entity.Category;
import com.example.demo_blog_api.entity.Comment;
import com.example.demo_blog_api.entity.Tag;
import com.example.demo_blog_api.entity.User;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

public final class ApiMapper {
    private ApiMapper() {
    }

    public static CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getSlug(),
                category.getDescription()
        );
    }

    public static TagResponse toTagResponse(Tag tag) {
        return new TagResponse(tag.getId(), tag.getName(), tag.getSlug());
    }

    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole() == null ? null : user.getRole().getName().name(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public static BlogResponse toBlogResponse(Blog blog) {
        Set<TagResponse> tags = blog.getTags().stream()
                .sorted(Comparator.comparing(Tag::getName))
                .map(ApiMapper::toTagResponse)
                .collect(Collectors.toCollection(java.util.LinkedHashSet::new));

        return new BlogResponse(
                blog.getId(),
                blog.getTitle(),
                blog.getSlug(),
                blog.getSummary(),
                blog.getContent(),
                toUserResponse(blog.getAuthor()),
                toCategoryResponse(blog.getCategory()),
                tags,
                blog.getStatus(),
                blog.getPublishedAt(),
                blog.getCreatedAt(),
                blog.getUpdatedAt()
        );
    }

    public static CommentResponse toCommentResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getBlog().getId(),
                comment.getBlog().getTitle(),
                toUserResponse(comment.getUser()),
                comment.getContent(),
                comment.getStatus(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
