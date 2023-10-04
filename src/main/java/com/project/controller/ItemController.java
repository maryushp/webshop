package com.project.controller;

import com.project.model.Item;
import com.project.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<Item> createItem(@RequestBody Item item) {
        Item createdItem = itemService.create(item);
        return ResponseEntity.created(URI.create("/item/" + createdItem.getId())).body(createdItem);
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAll(){
        return ResponseEntity.ok(itemService.getAll());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Item> getById(@PathVariable("id") int id) {
        return ResponseEntity.ok(itemService.get(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Item> updateCategory(@PathVariable("id") int id, @RequestBody Item item) {
        return ResponseEntity.ok(itemService.update(item, id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable("id") int id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}