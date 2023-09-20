package com.project.service;

import com.project.model.Category;
import com.project.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements CrudService<Category>{
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        return categoryRepository.selectAll();
    }

    @Override
    public Category get(int id) {
            return categoryRepository.selectById(id).orElseThrow(RuntimeException::new);
    }

    @Override
    @Transactional
    public Category create(Category category) {
        if(categoryRepository.insert(category)){
            return categoryRepository.selectById(categoryRepository.getId(category).orElseThrow(RuntimeException::new)).orElseThrow(RuntimeException::new);
        }else {
            throw new RuntimeException();
        }
    }

    @Override
    public Category update(Category category, int id) {
        return null;
    }

    @Override
    public void delete(int id) {

    }
}