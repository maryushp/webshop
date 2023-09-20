package com.project.service;

import com.project.model.Item;
import com.project.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService implements CrudService<Item>{
    private final ItemRepository itemRepository;

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
        if(itemRepository.insert(item)){
            return itemRepository.selectById(itemRepository.getId(item).orElseThrow(RuntimeException::new)).orElseThrow(RuntimeException::new);
        }else {
            throw new RuntimeException();
        }
    }

    @Override
    public Item update(Item item, int id) {
        return null;
    }

    @Override
    public void delete(int id) {

    }
}