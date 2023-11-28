package com.project.user.controller;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import com.project.order.model.OrderDTO;
import com.project.user.model.UserDTO;
import com.project.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAll(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    @GetMapping(value = "/{id}/orders")
    public ResponseEntity<Page<OrderDTO>> getUserOrders(@PathVariable("id") Long id,
                                                        @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.getOrders(id, pageable));
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long id, @RequestBody JsonMergePatch patch) {
        return ResponseEntity.ok(userService.update(id, patch));
    }

}