package com.project.repository;

import com.project.model.Category;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest {
    private CategoryRepository categoryRepository;
    private EmbeddedDatabase embeddedDatabase;

    @BeforeEach
    public void init() {
        this.embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("database/Structure.sql")
                .addScript("database/Data.sql")
                .build();
        this.categoryRepository = new CategoryRepository(new JdbcTemplate(embeddedDatabase));
    }

    @Test
    void selectAllSuccess() {
        Category cat1 = new Category(1, "Category1");
        Category cat2 = new Category(2, "Category2");
        assertEquals(List.of(cat1, cat2), categoryRepository.selectAll());
    }

    @Test
    void selectCategoryById() {
        Category category = new Category(1, "Category1");
        assertEquals(category, categoryRepository.selectById(1).orElseThrow());
    }

    @Test
    void selectCategoryWrongId() {
        assertEquals(Optional.empty(), categoryRepository.selectById(99));
    }

    @Test
    void insertCategory() {
        Category cat = new Category();
        cat.setName("TEST_CATEGORY");
        assertTrue(categoryRepository.insert(cat));
        int id = categoryRepository.getId(cat).orElseThrow();
        assertEquals(cat.getName(), categoryRepository.selectById(id).orElseThrow().getName());
    }

    @Test
    void updateCategory() {
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("name", "TEST");
        assertEquals("TEST", categoryRepository.update(objectMap, 1).orElseThrow().getName());
    }

    @Test
    void deleteCategory() {
        Category cat = categoryRepository.selectById(1).orElseThrow();
        categoryRepository.delete(1);
        assertFalse(categoryRepository.isExists(cat));
    }

    @Test
    void isExistsCategory() {
        Category cat = categoryRepository.selectById(1).orElseThrow();
        assertTrue(categoryRepository.isExists(cat));
    }

    @Test
    void isExistsAbsentCategory () {
        Category cat = new Category();
        cat.setName("TEST");
        assertFalse(categoryRepository.isExists(cat));
    }

    @AfterEach
    public void finish() {
        embeddedDatabase.shutdown();
    }


}