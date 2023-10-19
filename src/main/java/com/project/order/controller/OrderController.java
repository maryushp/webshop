package com.project.order.controller;

import com.project.order.model.OrderDTO;
import com.project.order.service.OrderService;
import com.project.orderitem.model.OrderItemDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid Set<OrderItemDTO> orderItemDTOSet) {
        OrderDTO createdOrder = orderService.create(orderItemDTOSet);
        return ResponseEntity.created(URI.create("/order/" + createdOrder.getId())).body(createdOrder);
    }

    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(orderService.getAll(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OrderDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.get(id));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable("id") Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}