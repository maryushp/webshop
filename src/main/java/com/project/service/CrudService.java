package com.project.service;

import java.util.List;

public interface CrudService<T> {
    List<T> getAll();

    T get(Long id);

    T create(T t);

    T update(T t, Long id);

    void delete(Long id);
}