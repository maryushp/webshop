package com.project.service;

import java.util.List;

public interface CrudService<T> {
    List<T> getAll();
    T get(int id);
    T create(T t);
    T update(T t, int id);
    void delete(int id);
}