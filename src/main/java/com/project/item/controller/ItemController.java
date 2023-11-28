package com.project.item.controller;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.item.model.ItemDTO;
import com.project.item.service.DefaultItemService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class ItemController {
    private final DefaultItemService itemService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ItemDTO> createItem(@RequestPart("image") @Nullable MultipartFile image,
                                              @RequestPart("item") @Valid ItemDTO itemDto) {
        ItemDTO createdItem = itemService.create(itemDto, image);
        return ResponseEntity.created(URI.create("/item/" + createdItem.getId())).body(createdItem);
    }

    @GetMapping
    public ResponseEntity<Page<ItemDTO>> getAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(itemService.getAll(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ItemDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(itemService.get(id));
    }

    @GetMapping("/search/by-categories")
    public ResponseEntity<Page<ItemDTO>> getByCategories(@RequestParam List<Long> catIds,
                                                         @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(itemService.getByCategories(catIds, pageable));
    }

    @GetMapping("/search/by-name")
    public ResponseEntity<Page<ItemDTO>> getItemsByPartialName(@RequestParam("name") String partialName,
                                                               @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(itemService.getItemsByPartialName(partialName, pageable));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<ItemDTO> updateCategory(@PathVariable("id") Long id,
                                                  @RequestPart("patch") @Nullable JsonMergePatch patch,
                                                  @RequestPart("image") @Nullable MultipartFile image) {
        return ResponseEntity.ok(itemService.update(id, patch, image));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") Long id) {
        itemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}