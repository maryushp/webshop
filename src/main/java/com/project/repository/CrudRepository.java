package com.project.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, E>{
    List<T> selectAll();
    List<E> selectDependenciesById(int id);
    Optional<T> selectById(int id);
    boolean insert(T t);
    T update(T t, int id);
    void delete(int id);
    Optional<Integer> getId(T t);

}