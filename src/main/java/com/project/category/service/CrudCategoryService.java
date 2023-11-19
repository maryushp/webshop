package com.project.category.service;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.category.model.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudCategoryService {
    Page<CategoryDTO> getAll(Pageable pageable);

    CategoryDTO get(Long id);

    CategoryDTO create(CategoryDTO categoryDto);

    CategoryDTO update(Long id, JsonMergePatch patch);

    void delete(Long id);
}
