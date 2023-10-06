package com.project.service;

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
    public Item update(Item item, Long id) {
        Item it =
                itemRepository.findById(id).orElseThrow(() -> new NoSuchElemException(MessageFormat.format(ITEM_NOT_FOUND, id)));
        if (item.getName() != null)
            it.setName(item.getName());
        if (item.getPrice() != null)
            it.setPrice(item.getPrice());
        if (item.getDescription() != null)
            it.setDescription(item.getDescription());
        if (item.getCategories() != null) {
            List<Category> actualCategories = item.getCategories();
            it.setCategories(categoryService.getExistedCategories(actualCategories));
        }

        return itemRepository.save(it);
    }

    @Override
    public void delete(Long id) {
        if (!itemRepository.existsById(id))
            throw new NoSuchElemException(MessageFormat.format(ITEM_NOT_FOUND, id));
        itemRepository.deleteById(id);
    }
}