package com.project.service;

import com.project.exceptionhandler.exceptions.InvalidElementException;
import com.project.exceptionhandler.exceptions.NoSuchElemException;
import com.project.exceptionhandler.exceptions.SuchElementAlreadyExists;
import com.project.model.Category;
import com.project.repository.CategoryRepository;
import com.project.utils.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public Category get(int id) {
        Optional<Category> opCategory = categoryRepository.selectById(id);
        if(opCategory.isPresent())
            return opCategory.get();
        throw new NoSuchElemException(MessageFormat.format("Category with id = {0} doesn''t exist", id));
    }

    @Override
    @Transactional
    public Category create(Category category) {
        if (!Validation.isCategoryValid(category))
            throw new InvalidElementException("Category is invalid, please check your params");
        if(categoryRepository.insert(category)){
            return categoryRepository.selectById(categoryRepository.getId(category)
                    .orElseThrow(() -> new NoSuchElemException("Unable to find category with name = " + category.getName())))
                    .orElseThrow(() -> new NoSuchElemException("Unable to find category with name = " + category.getName()));
        }else {
            throw new SuchElementAlreadyExists("Category with name = " + category.getName() + " already exists");
        }
    }

    @Override
    public Category update(Category category, int id) {
        if(!Validation.isCategoryValid(category))
            throw new InvalidElementException("Category is invalid, please check your params");
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("name", category.getName());
        return categoryRepository.update(objectMap, id)
                .orElseThrow(() -> new NoSuchElemException(MessageFormat.format("Category with id = {0} doesn''t exist", id)));
    }

    @Override
    public void delete(int id) {
        if (!categoryRepository.delete(id))
            throw new NoSuchElemException("Category with id = " + id + " doesn't exist");
    }
}