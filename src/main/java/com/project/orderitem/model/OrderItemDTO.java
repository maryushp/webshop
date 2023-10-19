package com.project.orderitem.model;

import com.project.item.model.ItemDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    @NotNull
    private ItemDTO item;
    @NotNull
    private int amount;
}