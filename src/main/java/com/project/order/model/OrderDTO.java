package com.project.order.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.orderitem.model.OrderItemDTO;
import com.project.user.model.UserDTO;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static com.project.utils.exceptionhandler.ExceptionMessages.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private BigDecimal cost;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    private UserDTO user;
    @NotEmpty(message = ORDER_MUST_HAVE_ITEMS)
    private Set<OrderItemDTO> orderItems;
}