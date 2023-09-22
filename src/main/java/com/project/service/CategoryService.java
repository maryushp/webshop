package com.project.service;

import com.project.model.Category;
import com.project.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

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
    public Category update(Map<String, String> objectMap, int id) {
        return categoryRepository.update(objectMap, id).orElseThrow(RuntimeException::new);
    }

    @Override
    public void delete(int id) {
        if (!categoryRepository.delete(id))
            throw new RuntimeException();
    }
}