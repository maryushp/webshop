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
public class CategoryRepository implements CrudRepository<Category, Item>{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> selectAll() {
        return jdbcTemplate.query(AppQuery.Category.SELECT_ALL_CATEGORIES, new BeanPropertyRowMapper<>(Category.class));
    }

    @Override
    public List<Item> selectDependenciesById(int id) {
        return null;
    }
}