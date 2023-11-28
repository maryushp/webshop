package com.project.order.service;

import com.project.order.model.OrderDTO;
import com.project.order.model.OrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderDTO create(OrderRequest orderRequest);

    Page<OrderDTO> getAll(Pageable pageable);

    OrderDTO get(Long id);

    Page<OrderDTO> getByUser(Long id, Pageable pageable);

    void delete(Long id);
}