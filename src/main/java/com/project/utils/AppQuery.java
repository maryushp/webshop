package com.project.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class AppQuery {
    @UtilityClass
    public static class Item{
        public static final String SELECT_ALL_ITEMS = "SELECT * FROM item";
        public static final String SELECT_ALL_CATEGORIES_BY_ITEM_ID = "SELECT cat.id,cat.name FROM item_has_category item_cat JOIN category cat WHERE cat.id = item_cat.category_id AND item_cat.item_id = ?";
        public static final String SELECT_ITEM_BY_ID = "SELECT * FROM item WHERE id = ?";
        public static final String INSERT_ITEM = "INSERT INTO item (name, price, description, creation_date) VALUES (?, ?, ?, ?)";
        public static final String SELECT_ITEM_ID = "SELECT id FROM item WHERE name = ? AND price = ? AND description = ?";
    }

    @UtilityClass
    public static class Category{
        public static final String SELECT_ALL_CATEGORIES = "SELECT * FROM category";
        public static final String SELECT_CATEGORY_BY_ID = "SELECT * FROM category WHERE id = ?";
        public static final String INSERT_CATEGORY = "INSERT INTO category (name) VALUES (?)";
        public static final String SELECT_CATEGORY_ID = "SELECT id FROM category WHERE name = ?";
    }
}