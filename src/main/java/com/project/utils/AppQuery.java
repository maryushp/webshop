package com.project.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppQuery {
    @UtilityClass
    public static class Item{
        public static final String SELECT_ALL_ITEMS = "SELECT * FROM item";
        public static final String SELECT_ALL_CATEGORIES_BY_ITEM_ID = "SELECT cat.id,cat.name FROM item_has_category item_cat JOIN category cat WHERE cat.id = item_cat.category_id AND item_cat.item_id = ?";
    }

    @UtilityClass
    public static class Category{
        public static final String SELECT_ALL_CATEGORIES = "SELECT * FROM category";
    }
}
