package com.project.utils.mappers;

import com.project.model.Category;
import com.project.model.CategoryDTO;
import com.project.model.Item;
import com.project.model.ItemDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityDtoMapper {
    CategoryDTO toCategoryDTO(Category category);
    Category toCategory(CategoryDTO categoryDTO);

    ItemDTO toItemDTO(Item item);
    Item toItem(ItemDTO itemDTO);
}