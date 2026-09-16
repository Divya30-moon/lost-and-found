package com.jsp.lostAndFound.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jsp.lostAndFound.dto.ItemRequestDTO;
import com.jsp.lostAndFound.dto.ItemResponseDTO;
import com.jsp.lostAndFound.dto.UpdateItemRequestDTO;
import com.jsp.lostAndFound.entity.Category;
import com.jsp.lostAndFound.entity.Item;
import com.jsp.lostAndFound.entity.ItemStatus;
import com.jsp.lostAndFound.entity.ItemType;
import com.jsp.lostAndFound.entity.User;
import com.jsp.lostAndFound.exception.CategoryNotFoundException;
import com.jsp.lostAndFound.exception.ItemNotFoundException;
import com.jsp.lostAndFound.exception.UserNotFoundException;
import com.jsp.lostAndFound.repository.CategoryRepository;
import com.jsp.lostAndFound.repository.ItemRepository;
import com.jsp.lostAndFound.repository.UserRepository;

@Service
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           CategoryRepository categoryRepository) {

        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ItemResponseDTO createItem(ItemRequestDTO itemRequestDTO) {

        validateEventDate(itemRequestDTO.getEventDate());

        User user = userRepository.findById(itemRequestDTO.getReportedById())
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id: "
                                + itemRequestDTO.getReportedById()
                ));

        Category category = categoryRepository
                .findById(itemRequestDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: "
                                + itemRequestDTO.getCategoryId()
                ));

        Item item = new Item();

        item.setTitle(itemRequestDTO.getTitle());
        item.setDescription(itemRequestDTO.getDescription());
        item.setItemType(itemRequestDTO.getItemType());
        item.setCategory(category);
        item.setColor(itemRequestDTO.getColor());
        item.setLocation(itemRequestDTO.getLocation());
        item.setEventDate(itemRequestDTO.getEventDate());

        // Server-controlled fields
        item.setStatus(ItemStatus.ACTIVE);
        item.setReportedBy(user);
        item.setCreatedAt(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);

        return convertToResponseDTO(savedItem);
    }

    @Override
    public ItemResponseDTO getItemById(Long id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(
                        "Item not found with id: " + id
                ));

        return convertToResponseDTO(item);
    }

    @Override
    public List<ItemResponseDTO> getAllItems() {

        return itemRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public List<ItemResponseDTO> getItemsByTypeAndStatus(
            ItemType itemType, ItemStatus status) {

        return itemRepository
                .findByItemTypeAndStatus(itemType, status)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public List<ItemResponseDTO> getItemsReportedByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException(
                    "User not found with id: " + userId
            );
        }

        return itemRepository.findByReportedById(userId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public List<ItemResponseDTO> getItemsByCategory(Long categoryId) {

        if (!categoryRepository.existsById(categoryId)) {
            throw new CategoryNotFoundException(
                    "Category not found with id: " + categoryId
            );
        }

        return itemRepository.findByCategoryId(categoryId)
                .stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    @Override
    public ItemResponseDTO updateItem(
            Long id, UpdateItemRequestDTO updateItemRequestDTO) {

        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(
                        "Item not found with id: " + id
                ));

        validateEventDate(updateItemRequestDTO.getEventDate());

        Category category = categoryRepository
                .findById(updateItemRequestDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: "
                                + updateItemRequestDTO.getCategoryId()
                ));

        existingItem.setTitle(updateItemRequestDTO.getTitle());
        existingItem.setDescription(updateItemRequestDTO.getDescription());
        existingItem.setItemType(updateItemRequestDTO.getItemType());
        existingItem.setCategory(category);
        existingItem.setColor(updateItemRequestDTO.getColor());
        existingItem.setLocation(updateItemRequestDTO.getLocation());
        existingItem.setEventDate(updateItemRequestDTO.getEventDate());

        // These remain unchanged:
        // status
        // reportedBy
        // createdAt

        Item updatedItem = itemRepository.save(existingItem);

        return convertToResponseDTO(updatedItem);
    }

    @Override
    public void deleteItem(Long id) {

        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException(
                        "Item not found with id: " + id
                ));

        itemRepository.delete(existingItem);
    }

    private void validateEventDate(LocalDate eventDate) {

        if (eventDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Lost/found date cannot be in the future"
            );
        }
    }

    private ItemResponseDTO convertToResponseDTO(Item item) {

        return new ItemResponseDTO(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getItemType(),
                item.getCategory().getId(),
                item.getCategory().getName(),
                item.getColor(),
                item.getLocation(),
                item.getEventDate(),
                item.getStatus(),
                item.getReportedBy().getId(),
                item.getReportedBy().getName(),
                item.getCreatedAt()
        );
    }
}