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
    public CommentResponse create(Long blogId, CommentRequest request) {
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + blogId));
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.userId()));

        Comment comment = new Comment();
        comment.setBlog(blog);
        comment.setUser(user);
        comment.setContent(request.content().trim());
        comment.setStatus(CommentStatus.VISIBLE);

        return ApiMapper.toCommentResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional
    public CommentResponse update(Long id, String content) {
        Comment comment = commentRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
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
    public void delete(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Comment not found with id: " + id);
        }
        commentRepository.deleteById(id);
    }

    private void assertBlogExists(Long blogId) {
        if (!blogRepository.existsById(blogId)) {
            throw new ResourceNotFoundException("Blog not found with id: " + blogId);
        }
    }
}
