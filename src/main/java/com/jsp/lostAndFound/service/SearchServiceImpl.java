package com.jsp.lostAndFound.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jsp.lostAndFound.dto.ItemResponseDTO;
import com.jsp.lostAndFound.dto.ItemSearchRequestDTO;
import com.jsp.lostAndFound.entity.Item;
import com.jsp.lostAndFound.repository.ItemRepository;
import com.jsp.lostAndFound.repository.specification.ItemSearchSpecification;

@Service
public class SearchServiceImpl implements SearchService {

    private final ItemRepository itemRepository;

    public SearchServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<ItemResponseDTO> searchItems(ItemSearchRequestDTO request) {

        validateDateRange(request);

        List<Item> items = itemRepository.findAll(
                ItemSearchSpecification.filterBy(request)
        );

        return items.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    private void validateDateRange(ItemSearchRequestDTO request) {

        LocalDate fromDate = request.getFromDate();
        LocalDate toDate = request.getToDate();

        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) {
            throw new IllegalArgumentException(
                    "From date cannot be after to date"
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