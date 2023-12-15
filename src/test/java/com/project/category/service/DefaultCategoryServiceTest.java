package com.project.category.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.category.model.Category;
import com.project.category.model.CategoryDTO;
import com.project.category.repository.CategoryRepository;
import com.project.utils.exceptionhandler.exceptions.ElementNotFoundException;
import com.project.utils.exceptionhandler.exceptions.InvalidUpdateRequest;
import com.project.utils.exceptionhandler.exceptions.SuchElementAlreadyExists;
import com.project.utils.mappers.EntityDtoMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultCategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private EntityDtoMapper entityDtoMapper;
    @InjectMocks
    private DefaultCategoryService defaultCategoryService;

    private static CategoryDTO categoryDto;
    private static Category category;
    private static Category categoryToUpdate;
    private static CategoryDTO categoryToUpdateDto;
    private static JsonMergePatch patch;


    @BeforeAll
    static void setUp() {
        categoryDto = new CategoryDTO();
        categoryDto.setName("category");

        category = new Category();
        category.setName("category");

        categoryToUpdate = new Category();
        categoryToUpdate.setName("categoryUPDATE");

        categoryToUpdateDto = new CategoryDTO();
        categoryToUpdateDto.setName("categoryUPDATE");

        patch = createPatch(categoryToUpdate);
    }

    @Test
    void createCategoryAlreadyExists() {
        when(entityDtoMapper.toCategory(categoryDto)).thenReturn(category);
        when(categoryRepository.exists(Example.of(category))).thenReturn(true);

        assertThrows(SuchElementAlreadyExists.class, () -> defaultCategoryService.create(categoryDto));
    }

    @Test
    void createSuccess() {
        when(entityDtoMapper.toCategory(categoryDto)).thenReturn(category);
        when(categoryRepository.exists(Example.of(category))).thenReturn(false);
        when(categoryRepository.save(category)).thenReturn(category);
        when(entityDtoMapper.toCategoryDTO(category)).thenReturn(categoryDto);

        assertEquals(categoryDto, defaultCategoryService.create(categoryDto));
    }

    @Test
    void getAllSuccess() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Category> page = new PageImpl<>(List.of(category, category));

        when(categoryRepository.findAll(pageable)).thenReturn(page);

        when(entityDtoMapper.toCategoryDTO(any())).thenReturn(categoryDto);

        assertEquals(new PageImpl<>(List.of(categoryDto, categoryDto)), defaultCategoryService.getAll(pageable));
    }

    @Test
    void getWrongId() {
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> defaultCategoryService.get(1L));
    }

    @Test
    void getSuccess() {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(entityDtoMapper.toCategoryDTO(category)).thenReturn(categoryDto);

        assertEquals(categoryDto, defaultCategoryService.get(1L));
    }

    @Test
    void updateWrongId() {
        when(categoryRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> defaultCategoryService.update(1L, patch));
    }

    @Test
    void updateCategoryAlreadyExists() {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(categoryRepository.getCategoryByName(categoryToUpdate.getName())).thenReturn(Optional.of(categoryToUpdate));

        assertThrows(SuchElementAlreadyExists.class, () -> defaultCategoryService.update(1L, patch));
    }

    @Test
    void updateInvalidPatch() {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        JsonMergePatch invalidPatch = createPatch(1);

        assertThrows(InvalidUpdateRequest.class, () -> defaultCategoryService.update(1L, invalidPatch));
    }

    @Test
    void updateSuccess() {
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(categoryRepository.getCategoryByName(categoryToUpdate.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(any())).thenReturn(categoryToUpdate);
        when(entityDtoMapper.toCategoryDTO(categoryToUpdate)).thenReturn(categoryToUpdateDto);

        assertEquals(categoryToUpdateDto, defaultCategoryService.update(1L, patch));
    }

    @Test
    void deleteWrongId() {
        when(categoryRepository.existsById(any())).thenReturn(false);

        assertThrows(ElementNotFoundException.class, () -> defaultCategoryService.delete(1L));
    }

    @Test
    void deleteSuccess() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        defaultCategoryService.delete(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void getExistingCategories() {
        when(categoryRepository.getCategoryByName(category.getName())).thenReturn(Optional.empty());

        Set<Category> categories = Set.of(category);

        assertThrows(ElementNotFoundException.class,
                () -> defaultCategoryService.getExistingCategories(categories));
    }

    @Test
    void getExistingCategoriesSuccess() {
        when(categoryRepository.getCategoryByName(category.getName())).thenReturn(Optional.of(category));

        Set<Category> categories = Set.of(category);

        assertEquals(Set.of(category), defaultCategoryService.getExistingCategories(categories));
    }

    private static JsonMergePatch createPatch(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return JsonMergePatch.fromJson(objectMapper.convertValue(object, JsonNode.class));
        } catch (JsonPatchException e) {
            throw new RuntimeException(e);
        }
    }
}