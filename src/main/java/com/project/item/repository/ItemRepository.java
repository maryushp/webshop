package com.project.item.repository;

import com.project.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item> findByNameContaining(String partialName, Pageable pageable);

    Page<Item> findByCategoriesIdIn(List<Long> catIds, Pageable pageable);
}