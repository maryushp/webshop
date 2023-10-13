package com.project.utils.mappers;

import com.project.category.model.Category;
import com.project.category.model.CategoryDTO;
import com.project.item.model.Item;
import com.project.item.model.ItemDTO;
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
    User toUser(UserDTO userDTO);
}