package com.jsp.lostAndFound.service;

import java.util.List;

import com.jsp.lostAndFound.dto.ItemRequestDTO;
import com.jsp.lostAndFound.dto.ItemResponseDTO;
import com.jsp.lostAndFound.dto.UpdateItemRequestDTO;
import com.jsp.lostAndFound.entity.ItemStatus;
import com.jsp.lostAndFound.entity.ItemType;

public interface ItemService {

    ItemResponseDTO createItem(ItemRequestDTO itemRequestDTO);

    ItemResponseDTO getItemById(Long id);

    List<ItemResponseDTO> getAllItems();

    List<ItemResponseDTO> getItemsByTypeAndStatus(
            ItemType itemType, ItemStatus status);

    List<ItemResponseDTO> getItemsReportedByUser(Long userId);

    List<ItemResponseDTO> getItemsByCategory(Long categoryId);

    ItemResponseDTO updateItem(
            Long id, UpdateItemRequestDTO updateItemRequestDTO);

    void deleteItem(Long id);
}