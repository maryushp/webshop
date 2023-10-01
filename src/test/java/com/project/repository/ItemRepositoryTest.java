package com.project.repository;

import com.project.model.Category;
import com.project.model.Item;
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

class ItemRepositoryTest {
    private ItemRepository itemRepository;
    private EmbeddedDatabase embeddedDatabase;

    @BeforeEach
    public void init() {
        this.embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("database/Structure.sql")
                .addScript("database/Data.sql")
                .build();
        this.itemRepository = new ItemRepository(new JdbcTemplate(embeddedDatabase));
    }

    @Test
    void selectAllItems() {
        Item item1 = new Item(1, "Item1", 12.0, "description", "2020-09-20 13:00:00.0", null);
        Item item2 = new Item(2, "Item2", 3.0, "description", "2020-09-25 18:00:00.0", null);
        assertEquals(List.of(item1, item2), itemRepository.selectAll());
    }

    @Test
    void selectDependenciesByItemId() {
        Category cat = new Category(1, "Category1");
        assertEquals(List.of(cat), itemRepository.selectDependenciesById(1));
    }

    @Test
    void selectByItemId() {
        Item item = new Item(1, "Item1", 12.0, "description", "2020-09-20 13:00:00.0", null);
        assertEquals(item, itemRepository.selectById(1).orElseThrow());
    }

    @Test
    void selectItemByWrongId() {
        assertEquals(Optional.empty(), itemRepository.selectById(99));
    }

    @Test
    void insertItem() {
        Item item = new Item();
        item.setName("Item3");
        item.setPrice(5.0);
        item.setDescription("descr");

        assertTrue(itemRepository.insert(item));

        Item resultItem = itemRepository.selectById(itemRepository.getId(item).orElseThrow()).orElseThrow();

        assertEquals(item.getName(), resultItem.getName());
        assertEquals(item.getPrice(), resultItem.getPrice());
        assertEquals(item.getDescription(), resultItem.getDescription());
    }

    @Test
    void updateItem() {
        Item item = new Item(1, "TEST", 12.0, "description", "2020-09-20 13:00:00.0", null);
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("name", "TEST");
        assertEquals(item, itemRepository.update(objectMap, 1).orElseThrow());
    }

    @Test
    void deleteItem() {
        Item item = itemRepository.selectById(1).orElseThrow();
        assertTrue(itemRepository.delete(1));
        assertFalse(itemRepository.isExists(item));
    }

    @Test
    void addCategoryToItemSuccess() {
        itemRepository.addCategoryToItem(1, 2);
        List<Category> categories = List.of(new Category(1, "Category1"), new Category(2, "Category2"));
        List<Category> resultCategories = itemRepository.selectDependenciesById(1);
        assertEquals(categories, resultCategories);
    }

    @Test
    void deleteItemDependenciesByIdSuccess() {
        itemRepository.deleteItemDependenciesById(1);
        assertEquals(List.of(), itemRepository.selectDependenciesById(1));
    }

    @AfterEach
    public void finish() {
        embeddedDatabase.shutdown();
    }
}