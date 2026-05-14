package com.example.demo_blog_api.service.impl;

import com.example.demo_blog_api.dto.ApiMapper;
import com.example.demo_blog_api.dto.BlogRequest;
import com.example.demo_blog_api.dto.BlogResponse;
import com.example.demo_blog_api.entity.Blog;
import com.example.demo_blog_api.entity.BlogStatus;
import com.example.demo_blog_api.entity.Category;
import com.example.demo_blog_api.entity.Tag;
import com.example.demo_blog_api.entity.User;
import com.example.demo_blog_api.exception.ResourceNotFoundException;
import com.example.demo_blog_api.repository.BlogRepository;
import com.example.demo_blog_api.repository.CategoryRepository;
import com.example.demo_blog_api.repository.TagRepository;
import com.example.demo_blog_api.repository.UserRepository;
import com.example.demo_blog_api.service.BlogService;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final Slugify slugify = Slugify.builder().build();

    @Override
    @Transactional(readOnly = true)
    public List<BlogResponse> getPublishedBlogs() {
        return blogRepository.findAllByStatusWithRelations(BlogStatus.PUBLISHED).stream()
                .map(ApiMapper::toBlogResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BlogResponse> getAllBlogs() {
        return blogRepository.findAllWithRelations().stream()
                .map(ApiMapper::toBlogResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BlogResponse getById(Long id) {
        return blogRepository.findByIdWithRelations(id)
                .map(ApiMapper::toBlogResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public BlogResponse getBySlug(String slug) {
        return blogRepository.findBySlug(slug)
                .map(ApiMapper::toBlogResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with slug: " + slug));
    }

    @Override
    @Transactional
    public BlogResponse create(BlogRequest request) {
        Blog blog = new Blog();
        applyRequest(blog, request, null);
        return ApiMapper.toBlogResponse(blogRepository.save(blog));
    }

    @Override
    @Transactional
    public BlogResponse update(Long id, BlogRequest request) {
        Blog blog = blogRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        applyRequest(blog, request, id);
        return ApiMapper.toBlogResponse(blogRepository.save(blog));
    }

    @Override
    @Transactional
    public BlogResponse updateStatus(Long id, BlogStatus status) {
        Blog blog = blogRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id: " + id));
        blog.setStatus(status);
        return ApiMapper.toBlogResponse(blogRepository.save(blog));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!blogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Blog not found with id: " + id);
        }
        blogRepository.deleteById(id);
    }

    private void applyRequest(Blog blog, BlogRequest request, Long currentId) {
        User author = userRepository.findById(request.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + request.authorId()));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + request.categoryId()));

        blog.setTitle(request.title().trim());
        blog.setSlug(uniqueSlug(request.title(), currentId));
        blog.setSummary(request.summary().trim());
        blog.setContent(request.content());
        blog.setAuthor(author);
        blog.setCategory(category);
        blog.setStatus(request.status() == null ? BlogStatus.DRAFT : request.status());
        blog.setTags(resolveTags(request.tagIds()));
    }

    private Set<Tag> resolveTags(Set<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return new HashSet<>();
        }

        Set<Long> distinctIds = new HashSet<>(tagIds);
        List<Tag> tags = tagRepository.findAllById(distinctIds);
        if (tags.size() != distinctIds.size()) {
            throw new ResourceNotFoundException("One or more tags were not found");
        }
        return new HashSet<>(tags);
    }

    private String uniqueSlug(String title, Long currentId) {
        String baseSlug = slugify.slugify(title);
        String candidate = baseSlug;
        int index = 1;
        while (blogRepository.existsBySlug(candidate)
                && blogRepository.findBySlug(candidate)
                .map(existing -> currentId == null || !existing.getId().equals(currentId))
                .orElse(false)) {
            candidate = baseSlug + "-" + index;
            index++;
        }
        return candidate;
    }
}
