package com.project.repository;

import com.project.model.Category;
import com.project.model.Item;
import com.project.utils.AppQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
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
    public Item update(Item item, int id) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Optional<Integer> getId(Item item) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(AppQuery.Item.SELECT_ITEM_ID, Integer.class, item.getName(), item.getPrice(), item.getDescription()));
    }
}