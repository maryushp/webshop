package com.project.category.service;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.category.model.Category;
import com.project.category.model.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface CategoryService {
    CategoryDTO create(CategoryDTO categoryDto);

    Page<CategoryDTO> getAll(Pageable pageable);

    CategoryDTO get(Long id);

    CategoryDTO update(Long id, JsonMergePatch patch);

    void delete(Long id);

    Set<Category> getExistingCategories(Set<Category> categories);
}
