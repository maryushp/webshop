package com.project.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.order.model.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudOrderService {
    OrderDTO create(OrderDTO orderDto);

    Page<OrderDTO> getAll(Pageable pageable);

    OrderDTO get(Long id);

    OrderDTO update(Long id, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException;

    void delete(Long id);
}