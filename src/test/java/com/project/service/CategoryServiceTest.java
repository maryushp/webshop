package com.project.service;

import com.project.exceptionhandler.exceptions.InvalidElementException;
import com.project.exceptionhandler.exceptions.NoSuchElemException;
import com.project.exceptionhandler.exceptions.SuchElementAlreadyExists;
import com.project.model.Category;
import com.project.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    public static final String TEST_CATEGORY = "TestCategory";
    public static final Integer ID1 = 1;
    @Mock
    private CategoryRepository categoryRepositoryMocked;
    @InjectMocks
    private CategoryService categoryServiceMocked;

    @Test
    void getAllCategoriesSuccess() {
        when(categoryRepositoryMocked.selectAll()).thenReturn(Collections.emptyList());
        List<Category> categories = categoryServiceMocked.getAll();
        assertTrue(categories.isEmpty());

    }

    @Test
    void createCategoryIsInvalid() {
        Category cat = new Category();
        InvalidElementException thrown = assertThrows(InvalidElementException.class, () -> categoryServiceMocked.create(cat));
        assertEquals("Category is invalid, please check your params", thrown.getMessage());
    }

    @Test
    void createCategoryWhichAlreadyExists() {
        Category cat = new Category();
        cat.setName(TEST_CATEGORY);
        when(categoryRepositoryMocked.isExists(cat)).thenReturn(true);
        SuchElementAlreadyExists thrown = assertThrows(SuchElementAlreadyExists.class, () -> categoryServiceMocked.create(cat));
        assertEquals("Category with name = " + TEST_CATEGORY + " already exists", thrown.getMessage());
    }

    @Test
    void createCategorySuccess() {
        Category cat = new Category();
        cat.setName(TEST_CATEGORY);
        when(categoryRepositoryMocked.insert(cat)).thenReturn(true);
        when(categoryRepositoryMocked.getId(cat)).thenReturn(Optional.of(ID1));
        when(categoryRepositoryMocked.selectById(ID1)).thenReturn(Optional.of(cat));
        assertEquals(cat.getName(), categoryServiceMocked.create(cat).getName());
    }

    @Test
    void getCategoryWithWrongId() {
        when(categoryRepositoryMocked.selectById(ID1)).thenReturn(Optional.empty());
        NoSuchElemException thrown = assertThrows(NoSuchElemException.class, () -> categoryServiceMocked.get(ID1));
        assertEquals("Category with id = " + ID1 + " doesn't exist", thrown.getMessage());
    }

    @Test
    void getCategorySuccess() {
        Category cat = new Category();
        cat.setName(TEST_CATEGORY);
        when(categoryRepositoryMocked.selectById(ID1)).thenReturn(Optional.of(cat));
        assertEquals(cat.getName(), categoryServiceMocked.get(ID1).getName());
    }

    @Test
    void deleteCategoryWithWrongId() {
        when(categoryRepositoryMocked.delete(ID1)).thenReturn(false);
        NoSuchElemException thrown = assertThrows(NoSuchElemException.class, () -> categoryServiceMocked.delete(ID1));
        assertEquals("Category with id = " + ID1 + " doesn't exist", thrown.getMessage());
    }

    @Test
    void updateCategoryIsInvalid() {
        Category cat = new Category();
        InvalidElementException thrown = assertThrows(InvalidElementException.class, () -> categoryServiceMocked.update(cat, ID1));
        assertEquals("Category is invalid, please check your params", thrown.getMessage());
    }

    @Test
    void updateCategoryWithWrongId() {
        Category cat = new Category();
        cat.setName(TEST_CATEGORY);
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("name", cat.getName());
        when(categoryRepositoryMocked.update(objectMap, ID1)).thenReturn(Optional.empty());
        NoSuchElemException thrown = assertThrows(NoSuchElemException.class, () -> categoryServiceMocked.update(cat, ID1));
        assertEquals("Category with id = " + ID1 + " doesn't exist", thrown.getMessage());
    }

    @Test
    void updateCategorySuccess() {
        Category cat = new Category();
        cat.setName(TEST_CATEGORY);
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("name", cat.getName());
        when(categoryRepositoryMocked.update(objectMap, ID1)).thenReturn(Optional.of(cat));
        assertEquals(TEST_CATEGORY, categoryServiceMocked.update(cat, ID1).getName());
    }

    @Test
    void updateCategoryNotUniqueName() {
        Category cat = new Category();
        cat.setName("TEST");
        when(categoryRepositoryMocked.isExists(cat)).thenReturn(true);
        assertThrows(SuchElementAlreadyExists.class, () -> categoryServiceMocked.update(cat, 1));
    }

}