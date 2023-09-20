package com.project.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CrudRepository<T, E>{
    List<T> selectAll();
    List<E> selectDependenciesById(int id);
    Optional<T> selectById(int id);
    boolean insert(T t);
    Optional<T> update(Map<String, String> objectMap, int id);
    boolean delete(int id);
    Optional<Integer> getId(T t);

}