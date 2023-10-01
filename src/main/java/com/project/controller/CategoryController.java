package com.project.controller;

import com.project.model.Category;
import com.project.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    ResponseEntity<List<Category>> getAll(){
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<Category> getById(@PathVariable("id") int id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        return new ResponseEntity<>(categoryService.create(category), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable("id") int id) {
        categoryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable("id") int id, @RequestBody Category category) {
        return ResponseEntity.ok(categoryService.update(category, id));
    }
}