package com.example.demo_blog_api.service.impl;

import com.example.demo_blog_api.dto.ApiMapper;
import com.example.demo_blog_api.dto.TagRequest;
import com.example.demo_blog_api.dto.TagResponse;
import com.example.demo_blog_api.entity.Tag;
import com.example.demo_blog_api.exception.DuplicateResourceException;
import com.example.demo_blog_api.exception.ResourceNotFoundException;
import com.example.demo_blog_api.repository.TagRepository;
import com.example.demo_blog_api.service.TagService;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final Slugify slugify = Slugify.builder().build();

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> getAll() {
        return tagRepository.findAll().stream()
                .map(ApiMapper::toTagResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TagResponse getById(Long id) {
        return tagRepository.findById(id)
                .map(ApiMapper::toTagResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
    }

    @Override
    @Transactional
    public TagResponse add(TagRequest request) {
        String slug = slugify.slugify(request.name());
        if (tagRepository.existsByNameIgnoreCase(request.name()) || tagRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException("Tag already exists: " + request.name());
        }

        Tag tag = new Tag();
        tag.setName(request.name().trim());
        tag.setSlug(slug);
        return ApiMapper.toTagResponse(tagRepository.save(tag));
    }

    @Override
    @Transactional
    public TagResponse update(TagRequest request, Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        String slug = slugify.slugify(request.name());

        tagRepository.findBySlug(slug)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new DuplicateResourceException("Tag already exists: " + request.name());
                });

        tag.setName(request.name().trim());
        tag.setSlug(slug);
        return ApiMapper.toTagResponse(tagRepository.save(tag));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tag not found with id: " + id);
        }
        tagRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagResponse> searchByName(String name) {
        return tagRepository.findByNameContainingIgnoreCase(name).stream()
                .map(ApiMapper::toTagResponse)
                .toList();
    }
}
