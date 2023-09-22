package com.project.repository;

import com.project.model.Category;
import com.project.model.Item;
import com.project.utils.AppQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ItemRepository implements CrudRepository<Item, Category>{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Item> selectAll() {
        return jdbcTemplate.query(AppQuery.Item.SELECT_ALL_ITEMS, new BeanPropertyRowMapper<>(Item.class));
    }

    @Override
    public List<Category> selectDependenciesById(int id) {
        return jdbcTemplate.query(AppQuery.Item.SELECT_ALL_CATEGORIES_BY_ITEM_ID, new Integer[]{id}, new BeanPropertyRowMapper<>(Category.class));
    }

    @Override
    public Optional<Item> selectById(int id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(AppQuery.Item.SELECT_ITEM_BY_ID, new Integer[]{id}, new BeanPropertyRowMapper<>(Item.class)));
    }

    @Override
    public boolean insert(Item item) {
        return jdbcTemplate.update(AppQuery.Item.INSERT_ITEM, item.getName(), item.getPrice(), item.getDescription(), LocalDateTime.now()) == 1;
    }

    @Override
    public Optional<Item> update(Map<String, String> objectMap, int id) {
        List<String> values = new ArrayList<>();
        StringBuilder query = new StringBuilder(AppQuery.Item.UPDATE_ITEM_BY_ID);

        objectMap.forEach((key, value) ->
        {       query.append(" ").append(key).append(" = ?, ");
                values.add(value);
        });
        query.setLength(query.length() - 2);
        query.append(" WHERE id = ?");
        values.add(String.valueOf(id));
        jdbcTemplate.update(query.toString(), values.toArray());
        return selectById(id);
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(AppQuery.Item.DELETE_ITEM_BY_ID, id) == 1;
    }

    @Override
    public Optional<Integer> getId(Item item) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(AppQuery.Item.SELECT_ITEM_ID, Integer.class, item.getName(), item.getPrice(), item.getDescription()));
    }

    @Override
    public boolean isExists(Item item) {
        return jdbcTemplate.queryForObject(AppQuery.Item.IS_ITEM_EXISTS, Integer.class, item.getName(), item.getPrice(), item.getDescription()) == 1;
    }

    public void addCategoryToItem(int itemId, int categoryId) {
        jdbcTemplate.update(AppQuery.ItemCategoryDependency.ADD_CATEGORY_TO_ITEM, itemId, categoryId);
    }

    public void deleteItemDependenciesById(int id) {
        jdbcTemplate.update(AppQuery.Item.DELETE_DEPENDENCY_BY_ITEM_ID, id);
    }
}