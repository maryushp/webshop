package com.project.service;

import com.project.model.Item;
import com.project.repository.CategoryRepository;
import com.project.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            item.setCategories(itemRepository.selectDependenciesById(opItem.get().getId()));
            return item;
        }else {
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional
    public Item create(Item item) {
        Item it;
        if(itemRepository.insert(item)){
            it = itemRepository.selectById(itemRepository.getId(item).orElseThrow(RuntimeException::new)).orElseThrow(RuntimeException::new);
        }else {
            throw new RuntimeException();
        }
        item.getCategories().forEach(category -> {
            if (categoryRepository.isExists(category)) {
                itemRepository.addCategory(it.getId(), categoryRepository.getId(category).orElseThrow(RuntimeException::new));
            }
        });
        return get(it.getId());
    }

    @Override
    public Item update(Map<String, String> objectMap, int id) {
        return itemRepository.update(objectMap, id).orElseThrow(RuntimeException::new);
    }

    @Override
    public void delete(int id) {
        if (!itemRepository.delete(id))
            throw new RuntimeException();
    }
}