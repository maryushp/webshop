package com.project.service;

import com.project.model.Category;
import com.project.repository.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements CrudService<Category>{
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        return categoryRepository.selectAll();
    }

    @Override
    public Optional<Category> get(int id) {
        return Optional.empty();
    }

    @Override
    public Category create(Category category) {
        return null;
    }

    @Override
    public Category update(Category category, int id) {
        return null;
    }

    @Override
    public void delete(int id) {

    }
}
