package com.project.utils.exceptionhandler;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ExceptionMessages {
    public static final String CATEGORY_NAME_SHOULD_BE_LESS_THAN_45_CHARS = "Category name should be less than 45 chars";
    public static final String CATEGORY_NAME_SHOULD_PRESENT_AND_NOT_BLANK = "Category name should present and shouldn't be blank";
    public static final String ITEM_NAME_SHOULD_BE_LESS_THAN_45_CHARS = "Item name should be less than 45 chars";
    public static final String ITEM_NAME_SHOULD_PRESENT_AND_NOT_BLANK = "Item name should present and shouldn't be blank";
    public static final String ITEM_PRICE_SHOULD_PRESENT = "Item price should present";
    public static final String ITEM_PRICE_SHOULD_BE_POSITIVE = "Item price should be positive";
    public static final String ITEM_DESCRIPTION_SHOULD_BE_LESS_THAN_500_CHARS = "Item description should be less than 500 chars";
    public static final String ITEM_DESCRIPTION_SHOULD_PRESENT = "Item description should present";
    public static final String ITEM_SHOULD_HAVE_AT_LEAST_1_CATEGORY = "Item should have at least 1 category";
    public static final String CATEGORY_NOT_FOUND_ID = "Category with id = {0} doesn''t exist";
    public static final String CATEGORY_NOT_FOUND_NAME = "Category with name = {0} doesn''t exist";
    public static final String CATEGORY_ALREADY_EXISTS = "Category with name = {0} already exists";
    public static final String ITEM_ALREADY_EXISTS = "Item with name = {0} already exists";
    public static final String ITEM_NOT_FOUND = "Item with id = {0} doesn''t exist";
    public static final String INVALID_ENTITY = "Entity is invalid, check your params";
    public static final String USER_EMAIL_SHOULD_BE_LESS_THAN_45_CHARS = "User email should be less than 45 chars";
    public static final String USER_EMAIL_SHOULD_PRESENT_AND_NOT_BLANK = "User email should present and shouldn't be blank";
    public static final String USER_NAME_SHOULD_BE_LESS_THAN_45_CHARS = "User name should be less than 45 chars";
    public static final String USER_NAME_SHOULD_PRESENT_AND_NOT_BLANK = "User name should present and shouldn't be blank";
    public static final String USER_SURNAME_SHOULD_BE_LESS_THAN_45_CHARS = "User surname should be less than 45 chars";
    public static final String USER_SURNAME_SHOULD_PRESENT_AND_NOT_BLANK = "User surname should present and shouldn't be blank";
    public static final String USER_NOT_FOUND = "User with id = {0} doesn''t exist";
    public static final String ORDER_MUST_HAVE_ITEMS = "Order must have items";
    public static final String ORDER_NOT_FOUND = "Order with id = {0} doesn''t exist";
    public static final String EMAIL_SHOULD_BE_VALID = "Email should be valid";
    public static final String SUCH_USER_EXISTS = "User with email = {0} already exists";
    public static final String PASSWORD_SHOULD_PRESENT_AND_NOT_BE_BLANK = "User password should present and shouldn't be blank";
    public static final String FORBIDDEN = "Forbidden";
    public static final String AMOUNT_SHOULD_BE_POSITIVE = "Amount should be positive";
    public static final String AMOUNT_SHOULD_PRESENT = "Amount should present";
    public static final String ITEM_SHOULD_PRESENT = "Item should present";
    public static final String ORDER_ITEMS_SHOULD_PRESENT = "Order items should present";
    public static final String ITEM_BY_PART_NOT_FOUND = "Item with partial name = {0} not found";
    public static final String IMAGE_OR_PATCH_MUST_PRESENT = "At least one parameter (image or patch) must present";
}