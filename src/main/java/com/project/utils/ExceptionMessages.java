package com.project.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ExceptionMessages {
    public static final String INVALID_CATEGORY = "Category is invalid, please check your params";
    public static final String CATEGORY_NOT_FOUND_ID = "Category with id = {0} doesn''t exist";
    public static final String CATEGORY_NOT_FOUND_NAME = "Category with name = {0} doesn''t exist";
    public static final String CATEGORY_ALREADY_EXISTS = "Category with name = {0} already exists";
    public static final String ITEM_NOT_FOUND = "Item with id = {0} doesn''t exist";
    public static final String INVALID_ITEM = "Item is invalid, please check your params";
    public static final String ITEM_ALREADY_EXISTS = "Item with name = {0} already exists";
}
