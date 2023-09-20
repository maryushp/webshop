package com.project.repository;

import com.project.model.Category;
import com.project.model.Item;
import com.project.utils.AppQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}