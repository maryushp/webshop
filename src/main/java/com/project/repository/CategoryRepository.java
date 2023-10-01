package com.project.repository;

import com.project.model.Category;
import com.project.utils.AppQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepository implements CrudRepository<Category> {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Category> selectAll() {
        return jdbcTemplate.query(AppQuery.Category.SELECT_ALL_CATEGORIES, new BeanPropertyRowMapper<>(Category.class));
    }

    @Override
    public Optional<Category> selectById(int id) {
        try {
            Category category = jdbcTemplate.queryForObject(
                    AppQuery.Category.SELECT_CATEGORY_BY_ID,
                    new Integer[]{id},
                    new BeanPropertyRowMapper<>(Category.class)
            );
            return Optional.ofNullable(category);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    @Override
    public boolean insert(Category category) {
        return jdbcTemplate.update(AppQuery.Category.INSERT_CATEGORY, category.getName()) == 1;
    }

    @Override
    public Optional<Category> update(Map<String, String> objectMap, int id) {
        jdbcTemplate.update(AppQuery.Category.UPDATE_CATEGORY_BY_ID, objectMap.get("name"), id);
        return selectById(id);
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(AppQuery.Category.DELETE_CATEGORY_BY_ID, id) == 1;
    }

    @Override
    public Optional<Integer> getId(Category category) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(AppQuery.Category.SELECT_CATEGORY_ID, Integer.class,
                category.getName()));
    }

    @Override
    public boolean isExists(Category category) {
        Integer result = jdbcTemplate.queryForObject(AppQuery.Category.IS_CATEGORY_EXISTS, Integer.class,
                category.getName());
        return result != null && result == 1;
    }
}