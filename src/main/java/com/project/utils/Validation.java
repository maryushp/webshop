package com.project.utils;

import com.project.model.Category;
import com.project.model.Item;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Validation {
    public static boolean isCategoryValid(Category category) {
        return category != null && category.getName() != null && !category.getName().isEmpty();
    }

    public static boolean areCategoriesValid(List<Category> categories) {
        if (categories != null){
            for (Category cat:categories) {
                if (!isCategoryValid(cat))
                    return false;
            }
        }
        return true;
    }

    public static boolean isItemValid(Item item) {
        return item != null && item.getName() != null && !item.getName().isEmpty()
                && item.getPrice() != null && item.getPrice() >= 0 && item.getDescription() != null
                && areCategoriesValid(item.getCategories());
    }
}