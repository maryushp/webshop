package com.project.item.service;

import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.item.model.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CrudItemService {
    Page<ItemDTO> getAll(Pageable pageable);

    ItemDTO get(Long id);

    ItemDTO create(ItemDTO itemDto, MultipartFile image) throws IOException;

    ItemDTO update(Long id, JsonMergePatch patch, MultipartFile image) throws JsonPatchException,
            IOException;

    void delete(Long id);
}
