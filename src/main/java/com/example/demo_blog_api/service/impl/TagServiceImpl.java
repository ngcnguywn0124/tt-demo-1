package com.example.demo_blog_api.service.impl;

import com.example.demo_blog_api.entity.Tag;
import com.example.demo_blog_api.repository.TagRepository;
import com.example.demo_blog_api.service.TagService;
import com.github.slugify.Slugify;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final Slugify slugify = Slugify.builder().build();

    public List<Tag> getAll(){
        return tagRepository.findAll();
    }

    public Optional<Tag> getById(Long id){
        return tagRepository.findById(id);
    }

    public Tag add(Tag tag){
        if (tag.getName() != null && !tag.getName().isEmpty()) {
            tag.setSlug(slugify.slugify(tag.getName()));
        }
        return tagRepository.save(tag);
    }

    public Tag update(Tag tag, Long id){
        return tagRepository.findById(id)
                .map(existingTag -> {
                    if (tag.getName() != null && !tag.getName().trim().isEmpty()) {
                        existingTag.setName(tag.getName());
                        existingTag.setSlug(slugify.slugify(tag.getName()));
                    }
                    return tagRepository.save(existingTag);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Tag với ID: " + id));
    }

    public void delete(Long id) {
        if (!tagRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy Tag với ID: " + id);
        }
        tagRepository.deleteById(id);
    }
}
