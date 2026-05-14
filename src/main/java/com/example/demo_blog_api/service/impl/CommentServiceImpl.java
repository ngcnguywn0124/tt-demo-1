package com.example.demo_blog_api.service.impl;

import com.example.demo_blog_api.dto.ApiMapper;
import com.example.demo_blog_api.dto.CommentRequest;
import com.example.demo_blog_api.dto.CommentResponse;
import com.example.demo_blog_api.entity.Blog;
import com.example.demo_blog_api.entity.Comment;
import com.example.demo_blog_api.entity.CommentStatus;
import com.example.demo_blog_api.entity.User;
import com.example.demo_blog_api.exception.ResourceNotFoundException;
import com.example.demo_blog_api.repository.BlogRepository;
import com.example.demo_blog_api.repository.CommentRepository;
import com.example.demo_blog_api.repository.UserRepository;
import com.example.demo_blog_api.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getVisibleByBlog(Long blogId) {
        assertBlogExists(blogId);
        return commentRepository.findByBlogIdAndStatusOrderByCreatedAtDesc(blogId, CommentStatus.VISIBLE).stream()
                .map(ApiMapper::toCommentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getAllByBlog(Long blogId) {
        assertBlogExists(blogId);
        return commentRepository.findByBlogIdOrderByCreatedAtDesc(blogId).stream()
                .map(ApiMapper::toCommentResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CommentResponse getById(Long id) {
        return commentRepository.findByIdWithRelations(id)
                .map(ApiMapper::toCommentResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
    }

    @Override
    @Transactional
    public CommentResponse create(Long blogId, CommentRequest request, String username) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + blogId));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Comment comment = new Comment();
        comment.setBlog(blog);
        comment.setUser(user);
        comment.setContent(request.content().trim());
        comment.setStatus(CommentStatus.VISIBLE);

        return ApiMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentResponse update(Long id, String content, String username, boolean admin) {
        Comment comment = commentRepository.findByIdWithBlogAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        assertCommentOwnerOrAdmin(comment, username, admin);
        comment.setContent(content.trim());
        return ApiMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentResponse updateStatus(Long id, CommentStatus status) {
        Comment comment = commentRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        comment.setStatus(status);
        return ApiMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public void delete(Long id, String username, boolean admin) {
        Comment comment = commentRepository.findByIdWithBlogAuthor(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
        assertCommentOwnerOrAdmin(comment, username, admin);
        commentRepository.delete(comment);
    }

    private void assertBlogExists(Long blogId) {
        if (!blogRepository.existsById(blogId)) {
            throw new ResourceNotFoundException("Blog not found with id: " + blogId);
        }
    }

    private void assertCommentOwnerOrAdmin(Comment comment, String username, boolean admin) {
        if (admin) {
            return;
        }
        if (!comment.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only modify your own comment");
        }
    }
}
