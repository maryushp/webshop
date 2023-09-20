package com.project.repository;

import java.util.List;

public interface CrudRepository<T, E>{
    List<T> selectAll();
    List<E> selectDependenciesById(int id);

}