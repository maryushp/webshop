package com.project.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.category.service.CategoryService;
import com.project.image.ImageService;
import com.project.utils.exceptionhandler.exceptions.InvalidUpdateRequest;
import com.project.utils.exceptionhandler.exceptions.ElementNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Service
@RequiredArgsConstructor
public class DefaultItemService implements ItemService {
    private final ItemRepository itemRepository;
    private final CategoryService categoryService;
    private final EntityDtoMapper entityDtoMapper;
    private final ImageService imageService;

    @Override
    @Transactional
    public ItemDTO create(ItemDTO itemDto, MultipartFile image) {
        Item item = entityDtoMapper.toItem(itemDto);

        if (itemRepository.exists(Example.of(item))) {
            throw new SuchElementAlreadyExists(MessageFormat.format(ITEM_ALREADY_EXISTS, item.getName()));
        }

        Set<Category> actualCategories = item.getCategories();
        item.setCategories(categoryService.getExistingCategories(actualCategories));

        item.setCreationDate(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);

        if (image != null) {
            String imageURI = imageService.uploadImage(image, savedItem.getId().toString());
            item.setImageData(imageURI);
            return entityDtoMapper.toItemDTO(itemRepository.save(item));
        }

        return entityDtoMapper.toItemDTO(savedItem);
    }

    @Override
    public Page<ItemDTO> getAll(Pageable pageable) {
        return itemRepository.findAll(pageable).map(entityDtoMapper::toItemDTO);
    }

    @Override
    public ItemDTO get(Long id) {
        ItemDTO item = entityDtoMapper.toItemDTO(itemRepository.findById(id)
                .orElseThrow(() -> new ElementNotFoundException(MessageFormat.format(ITEM_NOT_FOUND, id))
                ));

        item.setImageData((item.getImageData()));
        return item;
    }

    @Override
    public Page<ItemDTO> getByCategories(List<Long> catIds, Pageable pageable) {
        Page<Item> itemsByCategories = itemRepository.findByCategoriesIdIn(catIds, pageable);
        return itemsByCategories.map(entityDtoMapper::toItemDTO);
    }

    @Override
    public Page<ItemDTO> getItemsByPartialName(String partialName, Pageable pageable) {
        if (partialName == null || partialName.isEmpty()) {
            throw new ElementNotFoundException(MessageFormat.format(ITEM_BY_PART_NOT_FOUND, partialName));
        }

        Page<Item> items = itemRepository.findByNameContaining(partialName, pageable);


        if (items.isEmpty()) {
            throw new ElementNotFoundException(MessageFormat.format(ITEM_BY_PART_NOT_FOUND, partialName));
        }

        return items.map(entityDtoMapper::toItemDTO);
    }

    @Override
    @Transactional
    public ItemDTO update(Long id, JsonMergePatch patch, MultipartFile image) {
        if (patch == null && image == null) {
            throw new InvalidUpdateRequest(IMAGE_OR_PATCH_MUST_PRESENT);
        }

        Item dbItem =
                itemRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(MessageFormat.format(ITEM_NOT_FOUND, id)));

        if (patch != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModules(new JavaTimeModule());

            Item updatedItem;
            try {
                JsonNode updatedJson = patch.apply(objectMapper.convertValue(dbItem, JsonNode.class));
                updatedItem = objectMapper.treeToValue(updatedJson, Item.class);
            } catch (JsonPatchException | JsonProcessingException e) {
                throw new InvalidUpdateRequest(INVALID_ITEM_UPDATE);
            }

            dbItem.setName(updatedItem.getName());
            dbItem.setPrice(updatedItem.getPrice());
            dbItem.setDescription(updatedItem.getDescription());
            dbItem.setCategories(categoryService.getExistingCategories(updatedItem.getCategories()));
            dbItem.setLongDescription(updatedItem.getLongDescription());
        }

        if (image != null) {
            String imageURI = imageService.uploadImage(image, dbItem.getId().toString());
            dbItem.setImageData(imageURI);
        }

        return entityDtoMapper.toItemDTO(itemRepository.save(dbItem));
    }

    @Override
    public void delete(Long id) {
        Item item =
                itemRepository.findById(id).orElseThrow(() -> new ElementNotFoundException(MessageFormat.format(ITEM_NOT_FOUND, id)));

        String imageData = item.getImageData();
        if (imageData != null) {
            imageService.deleteImage(imageData);
        }

        itemRepository.deleteById(id);
    }
}