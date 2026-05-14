package com.example.demo_blog_api.service;

import com.example.demo_blog_api.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> getAll();
    Optional<Tag> getById(Long id);

    Tag add(Tag tag);

    Tag update(Tag tag, Long id);

    void delete(Long id);
}
