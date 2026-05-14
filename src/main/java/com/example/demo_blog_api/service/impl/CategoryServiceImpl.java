package com.example.demo_blog_api.service.impl;

import com.example.demo_blog_api.dto.ApiMapper;
import com.example.demo_blog_api.dto.CategoryRequest;
import com.example.demo_blog_api.dto.CategoryResponse;
import com.example.demo_blog_api.entity.Category;
import com.example.demo_blog_api.exception.DuplicateResourceException;
import com.example.demo_blog_api.exception.ResourceNotFoundException;
import com.example.demo_blog_api.repository.CategoryRepository;
import com.example.demo_blog_api.service.CategoryService;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final Slugify slugify = Slugify.builder().build();

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .map(ApiMapper::toCategoryResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        return categoryRepository.findById(id)
                .map(ApiMapper::toCategoryResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    @Transactional
    public CategoryResponse save(CategoryRequest request) {
        String slug = slugify.slugify(request.name());
        if (categoryRepository.existsByNameIgnoreCase(request.name()) || categoryRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException("Category already exists: " + request.name());
        }

        Category category = new Category();
        category.setName(request.name().trim());
        category.setDescription(request.description());
        category.setSlug(slug);
        return ApiMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryResponse update(CategoryRequest request, Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        String slug = slugify.slugify(request.name());

        categoryRepository.findBySlug(slug)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Category already exists: " + request.name());
                });

        category.setName(request.name().trim());
        category.setDescription(request.description());
        category.setSlug(slug);
        return ApiMapper.toCategoryResponse(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> searchByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name).stream()
                .map(ApiMapper::toCategoryResponse)
                .toList();
    }
}
