package com.project.repository;

import com.project.model.Category;
import com.project.model.Item;
import com.project.utils.AppQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepository implements CrudRepository<Category, Item>{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> selectAll() {
        return jdbcTemplate.query(AppQuery.Category.SELECT_ALL_CATEGORIES, new BeanPropertyRowMapper<>(Category.class));
    }

    @Override
    public List<Item> selectDependenciesById(int id) {
        return new ArrayList<>();
    }

    @Override
    public Optional<Category> selectById(int id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(AppQuery.Category.SELECT_CATEGORY_BY_ID, new Integer[]{id}, new BeanPropertyRowMapper<>(Category.class)));
    }

    @Override
    public boolean insert(Category category) {
        return jdbcTemplate.update(AppQuery.Category.INSERT_CATEGORY, category.getName()) == 1;
    }

    @Override
    public Category update(Category category, int id) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(AppQuery.Category.DELETE_CATEGORY_BY_ID, id) == 1;
    }

    @Override
    public Optional<Integer> getId(Category category) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(AppQuery.Category.SELECT_CATEGORY_ID, Integer.class, category.getName()));
    }
}