package com.project.item.service;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.item.model.ItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ItemService {
    ItemDTO create(ItemDTO itemDto, MultipartFile image);

    Page<ItemDTO> getAll(Pageable pageable);

    ItemDTO get(Long id);

    Page<ItemDTO> getByCategories(List<Long> catIds, Pageable pageable);

    Page<ItemDTO> getItemsByPartialName(String partialName, Pageable pageable);

    ItemDTO update(Long id, JsonMergePatch patch, MultipartFile image);

    void delete(Long id);
}
