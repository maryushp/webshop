package com.project.controller;

import com.project.model.Category;
import com.project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.create(category);
        return ResponseEntity.created(URI.create("/category/" + createdCategory.getId())).body(createdCategory);
    }

    @GetMapping
    public ResponseEntity<List<Category>> getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Category> getById(@PathVariable("id") int id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") int id, @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.update(category, id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("id") int id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}