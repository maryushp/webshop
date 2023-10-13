package com.project.item.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.item.model.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudItemService {
    Page<ItemDTO> getAll(Pageable pageable);

    ItemDTO get(Long id);

    ItemDTO create(ItemDTO itemDto);

    ItemDTO update(Long id, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException;

    void delete(Long id);
}
