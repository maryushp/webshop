package com.project.service;

import com.project.exceptionhandler.exceptions.InvalidElementException;
import com.project.exceptionhandler.exceptions.NoSuchElemException;
import com.project.exceptionhandler.exceptions.SuchElementAlreadyExists;
import com.project.model.Category;
import com.project.model.Item;
import com.project.repository.CategoryRepository;
import com.project.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    public static final String ITEM_NAME = "TestItem";
    public static final Category TEST_CATEGORY = new Category(1, "TestCategory");
    public static final Integer ID1 = 1;
    @Mock
    private ItemRepository itemRepositoryMocked;
    @Mock
    private CategoryRepository categoryRepositoryMocked;
    @InjectMocks
    private ItemService itemServiceMocked;

    @Test
    void getAllItemsSuccess() {
        Item item1 = new Item();
        Item item2 = new Item();
        item1.setId(1);
        item1.setName("Item 1");
        item2.setId(2);
        item2.setName("Item 2");

        when(itemRepositoryMocked.selectAll()).thenReturn(List.of(item1, item2));
        when(itemRepositoryMocked.selectDependenciesById(1)).thenReturn(List.of(new Category(1, "Category 1")));
        when(itemRepositoryMocked.selectDependenciesById(2)).thenReturn(List.of(new Category(2, "Category 2")));

        List<Item> result = itemServiceMocked.getAll();

        assertEquals(2, result.size());

        Item resultItem1 = result.get(0);
        assertEquals(1, resultItem1.getId());
        assertEquals("Item 1", resultItem1.getName());
        assertEquals(1, resultItem1.getCategories().size());

        Item resultItem2 = result.get(1);
        assertEquals(2, resultItem2.getId());
        assertEquals("Item 2", resultItem2.getName());
        assertEquals(1, resultItem2.getCategories().size());
    }

    @Test
    void getItemWithWrongId() {
        when(itemRepositoryMocked.selectById(ID1)).thenReturn(Optional.empty());
        NoSuchElemException thrown = assertThrows(NoSuchElemException.class, () -> itemServiceMocked.get(ID1));
        assertEquals("Item with id = " + ID1 + " doesn't exist", thrown.getMessage());
    }

    @Test
    void getItemSuccess() {
        Item item = new Item();
        item.setName(ITEM_NAME);
        item.setId(ID1);

        when(itemRepositoryMocked.selectById(ID1)).thenReturn(Optional.of(item));
        when(itemRepositoryMocked.selectDependenciesById(ID1)).thenReturn(List.of(TEST_CATEGORY));

        Item resultItem = itemServiceMocked.get(ID1);

        assertEquals(item.getName(), resultItem.getName());
        assertEquals(item.getCategories(), resultItem.getCategories());

    }

    @Test
    void deleteItemWithWrongId() {
        when(itemRepositoryMocked.delete(ID1)).thenReturn(false);
        NoSuchElemException thrown = assertThrows(NoSuchElemException.class, () -> itemServiceMocked.delete(ID1));
        assertEquals("Item with id = " + ID1 + " doesn't exist", thrown.getMessage());
    }

    @Test
    void createItemIsInvalid() {
        Item item = new Item();
        InvalidElementException thrown = assertThrows(InvalidElementException.class, () -> itemServiceMocked.create(item));
        assertEquals("Item is invalid, please check your params", thrown.getMessage());
    }

    @Test
    void createItemWhichAlreadyExist() {
        Item item = new Item(1, ITEM_NAME, 13.0, "q", "20.09.2013", List.of(TEST_CATEGORY));
        when(itemRepositoryMocked.insert(item)).thenReturn(false);
        SuchElementAlreadyExists thrown = assertThrows(SuchElementAlreadyExists.class, () -> itemServiceMocked.create(item));
        assertEquals("Item with name = " + item.getName() + " already exists", thrown.getMessage());

    }

    @Test
    void createItemSuccess() {
        Item item = new Item(1, ITEM_NAME, 13.0, "q", "20.09.2013", List.of(TEST_CATEGORY));
        when(itemRepositoryMocked.insert(item)).thenReturn(true);
        when(itemRepositoryMocked.getId(item)).thenReturn(Optional.of(ID1));
        when(itemRepositoryMocked.selectById(ID1)).thenReturn(Optional.of(item));
        when(categoryRepositoryMocked.isExists(TEST_CATEGORY)).thenReturn(true);
        when(categoryRepositoryMocked.getId(TEST_CATEGORY)).thenReturn(Optional.of(ID1));
        when(itemRepositoryMocked.selectDependenciesById(ID1)).thenReturn(List.of(TEST_CATEGORY));

        assertEquals(item, itemServiceMocked.create(item));

    }

    @Test
    void updateItemSuccess() {
        Item item = new Item();
        item.setName(ITEM_NAME);
        item.setCategories(List.of(TEST_CATEGORY));
        Map<String, String> objectMap = new HashMap<>();
        objectMap.put("name", ITEM_NAME);
        when(itemRepositoryMocked.update(objectMap, ID1)).thenReturn(Optional.empty());
        doNothing().when(itemRepositoryMocked).deleteItemDependenciesById(ID1);
        when(categoryRepositoryMocked.isExists(TEST_CATEGORY)).thenReturn(true);
        when(categoryRepositoryMocked.getId(TEST_CATEGORY)).thenReturn(Optional.of(ID1));
        doNothing().when(itemRepositoryMocked).addCategoryToItem(ID1, ID1);
        Item it = new Item();
        it.setName(ITEM_NAME);
        it.setId(ID1);
        when(itemRepositoryMocked.selectById(ID1)).thenReturn(Optional.of(it));
        when(itemRepositoryMocked.selectDependenciesById(ID1)).thenReturn(List.of(TEST_CATEGORY));

        Item resultItem = itemServiceMocked.update(item, ID1);
        assertEquals(item.getName(), resultItem.getName());
        assertEquals(item.getCategories(), resultItem.getCategories());


    }

}