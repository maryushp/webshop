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

import static com.project.utils.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class CategoryService implements CrudService<Category> {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAll() {
        return categoryRepository.selectAll();
    }

    @Override
    public Category get(int id) {
        return categoryRepository.selectById(id).orElseThrow(() -> new NoSuchElemException(
                MessageFormat.format(CATEGORY_NOT_FOUND, id)
        ));
    }

    @Override
    @Transactional
    public Category create(Category category) {
        if (!Validation.isCategoryValid(category))
            throw new InvalidElementException(INVALID_CATEGORY);
        if (!categoryRepository.isExists(category)) {
            categoryRepository.insert(category);
            return categoryRepository.selectById(categoryRepository.getId(category)
                            .orElseThrow(() -> new IllegalStateException(NON_EMPTY_ID)))
                    .orElseThrow(() -> new IllegalStateException(NON_EMPTY_CATEGORY));
        } else {
            throw new SuchElementAlreadyExists(MessageFormat.format(CATEGORY_ALREADY_EXISTS,
                    category.getName()));
        }
    }

    @Override
    @Transactional
    public Category update(Category category, int id) {
        if (!Validation.isCategoryValid(category))
            throw new InvalidElementException(INVALID_CATEGORY);
        if (categoryRepository.isExists(category))
            throw new SuchElementAlreadyExists(MessageFormat.format(CATEGORY_ALREADY_EXISTS,
                    category.getName()));
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("name", category.getName());
        return categoryRepository.update(objectMap, id)
                .orElseThrow(() -> new NoSuchElemException(MessageFormat.format(CATEGORY_NOT_FOUND,
                        id)));
    }

    @Override
    public void delete(int id) {
        if (!categoryRepository.delete(id))
            throw new NoSuchElemException(MessageFormat.format(CATEGORY_NOT_FOUND, id));
    }
}