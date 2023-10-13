package com.project.item.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.item.model.ItemDTO;
import com.project.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@RequestBody @Valid ItemDTO itemDto) {
        ItemDTO createdItem = itemService.create(itemDto);
        return ResponseEntity.created(URI.create("/item/" + createdItem.getId())).body(createdItem);
    }

    @GetMapping
    public ResponseEntity<Page<ItemDTO>> getAll(@PageableDefault Pageable pageable){
        return ResponseEntity.ok(itemService.getAll(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ItemDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemService.get(id));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ItemDTO> updateCategory(@PathVariable("id") Long id, @RequestBody JsonMergePatch patch) throws JsonPatchException, JsonProcessingException {
        return ResponseEntity.ok(itemService.update(id, patch));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}