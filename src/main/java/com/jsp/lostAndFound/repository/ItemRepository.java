package com.jsp.lostAndFound.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.jsp.lostAndFound.entity.Item;
import com.jsp.lostAndFound.entity.ItemStatus;
import com.jsp.lostAndFound.entity.ItemType;

public interface ItemRepository extends JpaRepository<Item, Long>, JpaSpecificationExecutor<Item> {

    List<Item> findByItemTypeAndStatus(ItemType itemType, ItemStatus status);

    List<Item> findByReportedById(Long userId);

    List<Item> findByCategoryId(Long categoryId);
}