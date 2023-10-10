package com.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;

import java.util.List;

public interface CrudService<T> {
    List<T> getAll();

    T get(Long id);

    T create(T t);

    T update(Long id, JsonMergePatch patch) throws JsonPatchException, JsonProcessingException;

    void delete(Long id);
}