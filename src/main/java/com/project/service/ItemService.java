package com.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.exceptionhandler.exceptions.InvalidElementException;
import com.project.exceptionhandler.exceptions.NoSuchElemException;
import com.project.exceptionhandler.exceptions.SuchElementAlreadyExists;
import com.project.model.Category;
import com.project.model.Item;
import com.project.repository.ItemRepository;
import com.project.utils.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;

import static com.project.utils.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class ItemService implements CrudService<Item> {
    private final ItemRepository itemRepository;
    private final CategoryService categoryService;

    @Override
    @Transactional
    public Item create(Item item) {
        if (!Validation.isItemValid(item))
            throw new InvalidElementException(INVALID_ITEM);

        if (itemRepository.exists(Example.of(item))) {
            throw new SuchElementAlreadyExists(MessageFormat.format(ITEM_ALREADY_EXISTS, item.getName()));
        }

        List<Category> actualCategories = item.getCategories();
        item.setCategories(categoryService.getExistedCategories(actualCategories));

        item.setCreationDate(LocalDateTime.now());

        return itemRepository.save(item);
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item get(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new NoSuchElemException(MessageFormat.format(ITEM_NOT_FOUND, id)));
    }

    @Override
    @Transactional
    public Item update(Long id, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        Item dbItem =
                itemRepository.findById(id).orElseThrow(() -> new NoSuchElemException(MessageFormat.format(ITEM_NOT_FOUND, id)));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModules(new JavaTimeModule());

        JsonNode updatedJson = patch.apply(objectMapper.convertValue(dbItem, JsonNode.class));
        Item updatedItem = objectMapper.treeToValue(updatedJson, Item.class);

        if (!Validation.isItemValid(updatedItem))
            throw new InvalidElementException(INVALID_ITEM);

        dbItem.setName(updatedItem.getName());
        dbItem.setPrice(updatedItem.getPrice());
        dbItem.setDescription(updatedItem.getDescription());
        dbItem.setCategories(updatedItem.getCategories());

        return itemRepository.save(dbItem);
    }

    @Override
    public void delete(Long id) {
        if (!itemRepository.existsById(id))
            throw new NoSuchElemException(MessageFormat.format(ITEM_NOT_FOUND, id));
        itemRepository.deleteById(id);
    }
}