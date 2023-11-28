package com.project.category.controller;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.category.model.CategoryDTO;
import com.project.category.service.DefaultCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {
    private final DefaultCategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CategoryDTO categoryDto) {
        CategoryDTO createdCategory = categoryService.create(categoryDto);
        return ResponseEntity.created(URI.create("/category/" + createdCategory.getId())).body(createdCategory);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> getAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(categoryService.getAll(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.get(id));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable("id") Long id, @RequestBody JsonMergePatch patch) {
        return ResponseEntity.ok(categoryService.update(id, patch));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}