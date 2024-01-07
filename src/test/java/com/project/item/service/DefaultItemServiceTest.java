package com.project.item.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.category.model.Category;
import com.project.category.service.CategoryService;
import com.project.image.ImageService;
import com.project.item.model.Item;
import com.project.item.model.ItemDTO;
import com.project.item.repository.ItemRepository;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private EntityDtoMapper entityDtoMapper;
    @Mock
    private CategoryService categoryService;
    @Mock
    private ImageService imageService;
    @InjectMocks
    private DefaultItemService defaultItemService;

    private static ItemDTO itemDto;
    private static Item item;
    private static JsonMergePatch patch;
    private static Set<Category> categories;
    private static MultipartFile mockedImage;

    @BeforeAll
    static void setUp() {
        createCategories();
        createItemDto();
        createItem();

        mockedImage = new MockMultipartFile("testFile", "testFile.jpg", "image/jpg", "Mock file content".getBytes());

        Item itemToUpdate = new Item();
        itemToUpdate.setName("itemUPDATE");
        itemToUpdate.setPrice(new BigDecimal(10));
        itemToUpdate.setDescription("description");
        itemToUpdate.setLongDescription("long description");
        itemToUpdate.setCategories(categories);
        patch = createPatch(itemToUpdate);
    }

    @Test
    void createItemAlreadyExists() {
        when(entityDtoMapper.toItem(itemDto)).thenReturn(item);
        when(itemRepository.exists(Example.of(item))).thenReturn(true);

        assertThrows(SuchElementAlreadyExists.class, () -> defaultItemService.create(itemDto, null));
    }

    @Test
    void createWithoutImageSuccess() {
        when(entityDtoMapper.toItem(itemDto)).thenReturn(item);
        when(itemRepository.exists(Example.of(item))).thenReturn(false);
        when(categoryService.getExistingCategories(any())).thenReturn(categories);
        when(itemRepository.save(item)).thenReturn(item);
        when(entityDtoMapper.toItemDTO(item)).thenReturn(itemDto);

        assertEquals(itemDto, defaultItemService.create(itemDto, null));
    }

    @Test
    void createWithImageSuccess() {
        Item savedItem = new Item();
        savedItem.setId(1L);

        when(entityDtoMapper.toItem(itemDto)).thenReturn(item);
        when(itemRepository.exists(Example.of(item))).thenReturn(false);
        when(categoryService.getExistingCategories(any())).thenReturn(categories);
        when(itemRepository.save(item)).thenReturn(savedItem);
        when(entityDtoMapper.toItemDTO(savedItem)).thenReturn(itemDto);
        when(imageService.uploadImage(mockedImage, savedItem.getId().toString())).thenReturn("imageURI");

        assertEquals(itemDto, defaultItemService.create(itemDto, mockedImage));
    }

    @Test
    void getAllSuccess() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Item> page = new PageImpl<>(List.of(item, item));

        when(itemRepository.findAll(pageable)).thenReturn(page);

        when(entityDtoMapper.toItemDTO(any())).thenReturn(itemDto);

        assertEquals(new PageImpl<>(List.of(itemDto, itemDto)), defaultItemService.getAll(pageable));
    }

    @Test
    void getWrongId() {
        when(itemRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> defaultItemService.get(1L));
    }

    @Test
    void getSuccess() {
        when(itemRepository.findById(any())).thenReturn(Optional.of(item));
        when(entityDtoMapper.toItemDTO(item)).thenReturn(itemDto);

        assertEquals(itemDto, defaultItemService.get(1L));
    }

    @Test
    void getByCategoriesSuccess() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Item> page = new PageImpl<>(List.of(item));

        when(itemRepository.findByCategoriesIdIn(List.of(1L, 2L), pageable)).thenReturn(page);

        when(entityDtoMapper.toItemDTO(item)).thenReturn(itemDto);

        assertEquals(new PageImpl<>(List.of(itemDto)), defaultItemService.getByCategories(List.of(1L, 2L), pageable));
    }

    @Test
    void getItemsByPartialNameEmptyName() {
        assertThrows(ElementNotFoundException.class, () -> defaultItemService.getItemsByPartialName("", null));
    }

    @Test
    void getItemsByPartialNameNullName() {
        assertThrows(ElementNotFoundException.class, () -> defaultItemService.getItemsByPartialName(null, null));
    }

    @Test
    void getItemsByPartialNameItemsNotFound() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Item> page = new PageImpl<>(List.of());

        when(itemRepository.findByNameContaining("item", pageable)).thenReturn(page);

        assertThrows(ElementNotFoundException.class, () -> defaultItemService.getItemsByPartialName("item", pageable));
    }

    @Test
    void getItemsByPartialNameSuccess() {
        Pageable pageable = PageRequest.of(0, 1);
        Page<Item> page = new PageImpl<>(List.of(item));

        when(itemRepository.findByNameContaining("item", pageable)).thenReturn(page);

        when(entityDtoMapper.toItemDTO(item)).thenReturn(itemDto);

        assertEquals(new PageImpl<>(List.of(itemDto)), defaultItemService.getItemsByPartialName("item", pageable));
    }

    @Test
    void updateNullParams() {
        assertThrows(InvalidUpdateRequest.class, () -> defaultItemService.update(1L, null, null));
    }

    @Test
    void updateWrongId() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> defaultItemService.update(1L, patch, mockedImage));
    }

    @Test
    void updateOnlyImageSuccess() {
        Item savedItem = new Item();
        savedItem.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(savedItem));
        when(imageService.uploadImage(mockedImage, savedItem.getId().toString())).thenReturn("imageURI");
        when(itemRepository.save(savedItem)).thenReturn(item);
        when(entityDtoMapper.toItemDTO(item)).thenReturn(itemDto);

        assertEquals(itemDto, defaultItemService.update(1L, null, mockedImage));
    }

    @Test
    void updateOnlyPatchSuccess() {
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.save(item)).thenReturn(item);
        when(entityDtoMapper.toItemDTO(item)).thenReturn(itemDto);

        assertEquals(itemDto, defaultItemService.update(1L, patch, null));
    }

    @Test
    void updateOnlyPatchInvalid() {
        JsonMergePatch invalidPatch = createPatch(1);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThrows(InvalidUpdateRequest.class, () -> defaultItemService.update(1L, invalidPatch, null));
    }

    @Test
    void updatePatchAndImageSuccess() {
        Item savedItem = new Item();
        savedItem.setId(1L);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(savedItem));
        when(imageService.uploadImage(mockedImage, savedItem.getId().toString())).thenReturn("imageURI");
        when(itemRepository.save(savedItem)).thenReturn(item);
        when(entityDtoMapper.toItemDTO(item)).thenReturn(itemDto);

        assertEquals(itemDto, defaultItemService.update(1L, patch, mockedImage));
    }

    @Test
    void deleteWrongId() {
        when(itemRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ElementNotFoundException.class, () -> defaultItemService.delete(1L));
    }

    @Test
    void deleteSuccess() {
        Item deleteItem = new Item();
        deleteItem.setImageData("uri");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(deleteItem));

        defaultItemService.delete(1L);

        verify(imageService).deleteImage(deleteItem.getImageData());
        verify(itemRepository).deleteById(1L);
    }

    private static void createItem() {
        item = new Item();
        item.setName("item");
        item.setPrice(new BigDecimal(10));
        item.setDescription("description");
        item.setLongDescription("long description");
        item.setCategories(categories);
    }

    private static void createItemDto() {
        itemDto = new ItemDTO();
        itemDto.setName("item");
        itemDto.setPrice(new BigDecimal(10));
        itemDto.setDescription("description");
        itemDto.setLongDescription("long description");
    }

    private static void createCategories() {
        Category category = new Category();
        category.setName("category");
        Category category2 = new Category();
        category2.setName("category2");
        categories = Set.of(category, category2);
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