package com.project.user.service;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.order.model.OrderDTO;
import com.project.user.model.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CrudUserService {
    Page<UserDTO> getAll(Pageable pageable);

    UserDTO get(Long id);

    Page<OrderDTO> getOrders(Long id, Pageable pageable);

    UserDTO update(Long id, JsonMergePatch patch);
}