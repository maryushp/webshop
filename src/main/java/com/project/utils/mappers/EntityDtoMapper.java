package com.project.utils.mappers;

import com.project.category.model.Category;
import com.project.category.model.CategoryDTO;
import com.project.item.model.Item;
import com.project.item.model.ItemDTO;
import com.project.order.model.Order;
import com.project.order.model.OrderDTO;
import com.project.orderitem.model.OrderItem;
import com.project.orderitem.model.OrderItemDTO;
import com.project.user.model.User;
import com.project.user.model.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityDtoMapper {
    CategoryDTO toCategoryDTO(Category category);
    Category toCategory(CategoryDTO categoryDTO);

    ItemDTO toItemDTO(Item item);
    Item toItem(ItemDTO itemDTO);

    UserDTO toUserDTO(User user);

    OrderDTO toOrderDTO(Order order);

    OrderItemDTO toOrderItemDTO(OrderItem orderItem);
    OrderItem toOrderItem(OrderItemDTO orderItemDTO);
}