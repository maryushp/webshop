package com.project.service;

import com.project.exceptionhandler.exceptions.InvalidElementException;
import com.project.exceptionhandler.exceptions.NoSuchElemException;
import com.project.exceptionhandler.exceptions.SuchElementAlreadyExists;
import com.project.model.Category;
import com.project.repository.CategoryRepository;
import com.project.utils.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import static com.project.utils.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class CategoryService implements CrudService<Category> {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Category create(Category category) {
        if (!Validation.isCategoryValid(category))
            throw new InvalidElementException(INVALID_CATEGORY);
        if (categoryRepository.exists(Example.of(category)))
            throw new SuchElementAlreadyExists(MessageFormat.format(CATEGORY_ALREADY_EXISTS,
                    category.getName()));
        return categoryRepository.save(category);

    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category get(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NoSuchElemException(
                MessageFormat.format(CATEGORY_NOT_FOUND_ID, id)
        ));
    }

    @Override
    @Transactional
    public Category update(Category category, Long id) {
        if (!Validation.isCategoryValid(category))
            throw new InvalidElementException(INVALID_CATEGORY);

        Category cat = categoryRepository.findById(id).orElseThrow(() -> new NoSuchElemException(
                MessageFormat.format(CATEGORY_NOT_FOUND_ID, id)));

        cat.setName(category.getName());

        return categoryRepository.save(cat);
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id))
            throw new NoSuchElemException(MessageFormat.format(CATEGORY_NOT_FOUND_ID, id));
        categoryRepository.deleteById(id);
    }

    public List<Category> getExistedCategories(List<Category> categories) {
        ArrayList<Category> existedCategories = new ArrayList<>();
        for (Category cat : categories) {
            existedCategories.add(categoryRepository.getCategoryByName(cat.getName()).orElseThrow(() -> new NoSuchElemException(
                    MessageFormat.format(CATEGORY_NOT_FOUND_NAME, cat.getName()))));
        }
        return existedCategories;
    }
}