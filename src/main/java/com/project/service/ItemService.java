package com.project.service;

import com.project.exceptionhandler.exceptions.InvalidElementException;
import com.project.exceptionhandler.exceptions.NoSuchElemException;
import com.project.exceptionhandler.exceptions.SuchElementAlreadyExists;
import com.project.model.Item;
import com.project.repository.CategoryRepository;
import com.project.repository.ItemRepository;
import com.project.utils.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService implements CrudService<Item>{
    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Item> getAll() {
        List<Item> items = itemRepository.selectAll();
        items.forEach(item -> item.setCategories(itemRepository.selectDependenciesById(item.getId())));
        return items;
    }

    @Override
    public Item get(int id) {
        Optional<Item> opItem = itemRepository.selectById(id);
        if(opItem.isPresent()){
            Item item = opItem.get();
            item.setCategories(itemRepository.selectDependenciesById(id));
            return item;
        }else {
            throw new NoSuchElemException(MessageFormat.format("Item with id = {0} doesn''t exist", id));
        }
    }

    @Override
    @Transactional
    public Item create(Item item) {
        if (!Validation.isItemValid(item))
            throw new InvalidElementException("Item is invalid, please check your params");
        Item it;
        if(itemRepository.insert(item)){
            it = itemRepository.selectById(itemRepository.getId(item)
                    .orElseThrow(() -> new NoSuchElemException(MessageFormat.format("Unable to find Item with name = {0}", item.getName()))))
                    .orElseThrow(() -> new NoSuchElemException(MessageFormat.format("Unable to find Item with name = {0}", item.getName())));
        }else {
            throw new SuchElementAlreadyExists("Item with name = " + item.getName() + " already exists");
        }
        item.getCategories().forEach(category -> {
            if (categoryRepository.isExists(category))
                itemRepository.addCategoryToItem(it.getId(), categoryRepository.getId(category)
                        .orElseThrow(() -> new NoSuchElemException("Unable to find category with name = " + category.getName())));
        });
        it.setCategories(itemRepository.selectDependenciesById(it.getId()));
        return it;
    }

    @Override
    public Item update(Item item, int id) {
        Map<String, String> objectMap = Validation.itemToMap(item);
        if(!objectMap.isEmpty())
            itemRepository.update(objectMap, id);
        if(item.getCategories() != null) {
            itemRepository.deleteItemDependenciesById(id);
            item.getCategories().forEach(category -> {
                if (categoryRepository.isExists(category))
                    itemRepository.addCategoryToItem(id, categoryRepository.getId(category)
                            .orElseThrow(() -> new NoSuchElemException("Unable to find category with name = " + category.getName())));
            });
        }
        return get(id);
    }

    @Override
    public void delete(int id) {
        if (!itemRepository.delete(id))
            throw new NoSuchElemException("Item with id = " + id + " doesn't exist");
    }
}