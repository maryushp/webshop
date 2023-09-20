package com.project.service;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {
    List<T> getAll();
    Optional<T> get(int id);
    T create(T t);
    T update(T t, int id);
    void delete(int id);
}