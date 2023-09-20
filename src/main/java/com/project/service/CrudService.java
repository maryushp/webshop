package com.project.service;

import java.util.List;
import java.util.Map;

public interface CrudService<T> {
    List<T> getAll();
    T get(int id);
    T create(T t);
    T update(Map<String, String> objectMap, int id);
    void delete(int id);
}