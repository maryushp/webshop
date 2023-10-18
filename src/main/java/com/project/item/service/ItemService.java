package com.project.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.category.service.CategoryService;
import com.project.utils.exceptionhandler.exceptions.NoSuchElemException;
import com.project.utils.exceptionhandler.exceptions.SuchElementAlreadyExists;
import com.project.category.model.Category;
import com.project.item.model.Item;
import com.project.item.model.ItemDTO;
import com.project.item.repository.ItemRepository;
import com.project.utils.mappers.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class ItemService implements CrudItemService {
    private final ItemRepository itemRepository;
    private final CategoryService categoryService;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    @Transactional
    public ItemDTO create(ItemDTO itemDto) {
        Item item = entityDtoMapper.toItem(itemDto);

        if (itemRepository.exists(Example.of(item))) {
            throw new SuchElementAlreadyExists(MessageFormat.format(ITEM_ALREADY_EXISTS, item.getName()));
        }

        List<Category> actualCategories = item.getCategories();
        item.setCategories(categoryService.getExistedCategories(actualCategories));

        item.setCreationDate(LocalDateTime.now());

        return entityDtoMapper.toItemDTO(itemRepository.save(item));
    }

    @Override
    public Page<ItemDTO> getAll(Pageable pageable) {
        return itemRepository.findAll(pageable).map(entityDtoMapper::toItemDTO);
    }

    @Override
    public ItemDTO get(Long id) {
        return entityDtoMapper.toItemDTO(itemRepository.findById(id)
                .orElseThrow(() -> new NoSuchElemException(MessageFormat.format(ITEM_NOT_FOUND, id))
                ));
    }

    public Page<ItemDTO> getByCategories(List<Long> catIds, Pageable pageable) {
        Page<Item> itemsByCategories = itemRepository.findByCategoriesIdIn(catIds, pageable);
        return itemsByCategories.map(entityDtoMapper::toItemDTO);
    }

    @Override
    @Transactional
    public ItemDTO update(Long id, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        Item dbItem =
                itemRepository.findById(id).orElseThrow(() -> new NoSuchElemException(MessageFormat.format(ITEM_NOT_FOUND, id)));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModules(new JavaTimeModule());

        JsonNode updatedJson = patch.apply(objectMapper.convertValue(dbItem, JsonNode.class));
        Item updatedItem = objectMapper.treeToValue(updatedJson, Item.class);

        dbItem.setName(updatedItem.getName());
        dbItem.setPrice(updatedItem.getPrice());
        dbItem.setDescription(updatedItem.getDescription());
        dbItem.setCategories(updatedItem.getCategories());

        return entityDtoMapper.toItemDTO(itemRepository.save(dbItem));
    }

    @Override
    public void delete(Long id) {
        if (!itemRepository.existsById(id))
            throw new NoSuchElemException(MessageFormat.format(ITEM_NOT_FOUND, id));
        itemRepository.deleteById(id);
    }

    public List<Item> getExistedItems(List<Item> items) {
        ArrayList<Item> existedItems = new ArrayList<>();
        for (Item item : items) {
            existedItems.add(itemRepository.getItemByName(item.getName()).orElseThrow(() -> new NoSuchElemException(
                    MessageFormat.format(ITEM_NOT_FOUND_NAME, item.getName()))));
        }
        return existedItems;
    }
}