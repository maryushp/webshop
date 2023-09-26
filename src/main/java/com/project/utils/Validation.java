package com.project.utils;

import com.project.model.Category;
import com.project.model.Item;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class Validation {
    public static Map<String, String> itemToMap (Item item) {
        Map<String, String> resultMap = new HashMap<>();
        if (item.getName() != null)
            resultMap.put("name", item.getName());
        if (item.getPrice() != null)
            resultMap.put("price", item.getPrice().toString());
        if (item.getDescription() != null)
            resultMap.put("description", item.getDescription());
        return resultMap;
    }

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
