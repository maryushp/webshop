package com.project.category.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.utils.exceptionhandler.exceptions.InvalidUpdateRequest;
import com.project.utils.exceptionhandler.exceptions.NoSuchElemException;
import com.project.utils.exceptionhandler.exceptions.SuchElementAlreadyExists;
import com.project.category.model.Category;
import com.project.category.model.CategoryDTO;
import com.project.category.repository.CategoryRepository;
import com.project.utils.mappers.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class DefaultCategoryService implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    @Transactional
    public CategoryDTO create(CategoryDTO categoryDto) {
        Category category = entityDtoMapper.toCategory(categoryDto);

        if (categoryRepository.exists(Example.of(category)))
            throw new SuchElementAlreadyExists(MessageFormat.format(CATEGORY_ALREADY_EXISTS,
                    category.getName()));

        return entityDtoMapper.toCategoryDTO(categoryRepository.save(category));

    }

    @Override
    public Page<CategoryDTO> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(entityDtoMapper::toCategoryDTO);
    }

    @Override
    public CategoryDTO get(Long id) {
        return entityDtoMapper.toCategoryDTO(categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElemException(MessageFormat.format(CATEGORY_NOT_FOUND_ID, id))
        ));
    }

    @Override
    @Transactional
    public CategoryDTO update(Long id, JsonMergePatch patch) {
        Category dbCategory = categoryRepository.findById(id).orElseThrow(() -> new NoSuchElemException(
                MessageFormat.format(CATEGORY_NOT_FOUND_ID, id)));

        ObjectMapper objectMapper = new ObjectMapper();

        Category updatedCategory;
        try {
            JsonNode updatedJson = patch.apply(objectMapper.convertValue(dbCategory, JsonNode.class));
            updatedCategory = objectMapper.treeToValue(updatedJson, Category.class);
        } catch(JsonPatchException | JsonProcessingException e) {
            throw new InvalidUpdateRequest(INVALID_CATEGORY_UPDATE);
        }

        if (categoryRepository.getCategoryByName(updatedCategory.getName()).isPresent())
            throw new SuchElementAlreadyExists(MessageFormat.format(CATEGORY_ALREADY_EXISTS, updatedCategory.getName()));

        dbCategory.setName(updatedCategory.getName());

        return entityDtoMapper.toCategoryDTO(categoryRepository.save(dbCategory));
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id))
            throw new NoSuchElemException(MessageFormat.format(CATEGORY_NOT_FOUND_ID, id));
        categoryRepository.deleteById(id);
    }

    public Set<Category> getExistedCategories(Set<Category> categories) {
        HashSet<Category> existedCategories = new HashSet<>();
        for (Category cat : categories) {
            existedCategories.add(categoryRepository.getCategoryByName(cat.getName()).orElseThrow(() -> new NoSuchElemException(
                    MessageFormat.format(CATEGORY_NOT_FOUND_NAME, cat.getName()))));
        }
        return existedCategories;
    }
}