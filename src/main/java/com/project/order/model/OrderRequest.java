package com.project.order.model;

import com.project.orderitem.model.OrderItemDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    @NotEmpty(message = ORDER_ITEMS_SHOULD_PRESENT)
    @Valid
    private Set<OrderItemDTO> orderItems;
}