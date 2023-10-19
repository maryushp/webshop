package com.project.orderitem.model;

import com.project.item.model.ItemDTO;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Positive;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {
    @NotNull(message = ITEM_SHOULD_PRESENT)
    private ItemDTO item;
    @NotNull(message = AMOUNT_SHOULD_PRESENT)
    @Positive(message = AMOUNT_SHOULD_BE_POSITIVE)
    private int amount;
}