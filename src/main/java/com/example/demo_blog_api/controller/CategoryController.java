package com.example.demo_blog_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo_blog_api.entity.Category;
import com.example.demo_blog_api.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<Category>> findAll(){
        return ResponseEntity.ok(categoryService.findAll());
    }
    @GetMapping(value = "/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id){
        return categoryService.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());

//        Optional<Category> category = categoryService.findById(id);
//        if(category.isPresent()){
//            return ResponseEntity.ok(category.get());
//        }else{
//            return ResponseEntity.notFound().build();
//        }
    }

    @PostMapping
    public ResponseEntity<Category> save(@RequestBody Category category){
        return ResponseEntity.ok(categoryService.save(category));
    }
    @PutMapping("/{id}") // Sử dụng PutMapping và khai báo biến {id} trên URL
    public ResponseEntity<Category> update(@RequestBody Category category, @PathVariable Long id) {
        // Gọi chính xác hàm update truyền vào cả đối tượng và id
        Category updatedCategory = categoryService.update(category, id);
        return ResponseEntity.ok(updatedCategory);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping(value = "/search/{keyword}")
    public ResponseEntity<Category> findByName(@PathVariable String keyword){
        return categoryService.findByName(keyword).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}
