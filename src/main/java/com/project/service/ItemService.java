package com.project.service;

import com.project.model.Item;
import com.project.repository.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public Optional<Item> get(int id) {
        return Optional.empty();
    }

    @Override
    public Item create(Item item) {
        return null;
    }

    @Override
    public Item update(Item item, int id) {
        return null;
    }

    @Override
    public void delete(int id) {

    }
}