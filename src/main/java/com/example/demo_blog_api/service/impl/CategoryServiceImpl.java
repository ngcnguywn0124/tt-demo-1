package com.example.demo_blog_api.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo_blog_api.entity.Category;
import com.example.demo_blog_api.repository.CategoryRepository;
import com.example.demo_blog_api.service.CategoryService;
import com.github.slugify.Slugify;

import lombok.RequiredArgsConstructor;

//final khac const vi no co the duoc gan 1 lan
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final Slugify slugify = Slugify.builder().build();

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long id){
        return categoryRepository.findById(id);
    }

    public Category save(Category category){
        if (category.getName() != null && !category.getName().isEmpty()) {
            category.setSlug(slugify.slugify(category.getName()));
        }
        return categoryRepository.save(category);
    }

    public Category update(Category category, Long id){
        return categoryRepository.findById(id)
                .map(existingCategory -> {
                    if (category.getName() != null && !category.getName().trim().isEmpty()) {
                        existingCategory.setName(category.getName());
                        existingCategory.setSlug(slugify.slugify(category.getName()));
                    }
                    return categoryRepository.save(existingCategory);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Category với ID: " + id));
    }


    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy Category với ID: " + id);
        }
        categoryRepository.deleteById(id);
    }

    public Optional<Category> findByName(String name){
        return categoryRepository.findByNameLike("%" + name +"%");
    }
}
