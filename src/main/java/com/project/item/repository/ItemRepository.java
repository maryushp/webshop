package com.project.item.repository;

import com.project.item.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> getItemByName(String name);

    Page<Item> findByCategoriesIdIn(List<Long> catIds, Pageable pageable);
}