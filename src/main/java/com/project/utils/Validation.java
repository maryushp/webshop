package com.project.utils;

import com.project.model.Item;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
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

}
