package com.example.demo_blog_api.controller;

import com.example.demo_blog_api.entity.Tag;
import com.example.demo_blog_api.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<Tag>> getAll(){
        return ResponseEntity.ok(tagService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tag> getById(@PathVariable Long id){
        return tagService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tag> add(@RequestBody Tag tag){
        return ResponseEntity.ok(tagService.add(tag));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tag> update(@RequestBody Tag tag, @PathVariable Long id) {
        return ResponseEntity.ok(tagService.update(tag, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
