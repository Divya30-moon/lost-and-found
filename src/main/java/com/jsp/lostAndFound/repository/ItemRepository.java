package com.jsp.lostAndFound.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jsp.lostAndFound.entity.Item;
import com.jsp.lostAndFound.entity.ItemStatus;
import com.jsp.lostAndFound.entity.ItemType;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByItemTypeAndStatus(ItemType itemType, ItemStatus status);

    List<Item> findByReportedById(Long userId);

    List<Item> findByCategoryId(Long categoryId);
}