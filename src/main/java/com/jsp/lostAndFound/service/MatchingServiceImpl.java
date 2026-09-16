package com.jsp.lostAndFound.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jsp.lostAndFound.dto.MatchResponseDTO;
import com.jsp.lostAndFound.entity.Item;
import com.jsp.lostAndFound.entity.ItemStatus;
import com.jsp.lostAndFound.entity.ItemType;
import com.jsp.lostAndFound.exception.ItemNotFoundException;
import com.jsp.lostAndFound.repository.ItemRepository;

@Service
public class MatchingServiceImpl implements MatchingService {

    private final ItemRepository itemRepository;

    public MatchingServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public List<MatchResponseDTO> findMatches(Long lostItemId) {

        Item lostItem = itemRepository.findById(lostItemId)
                .orElseThrow(() ->
                        new ItemNotFoundException(
                                "Lost item not found with id: " + lostItemId
                        )
                );

        if (lostItem.getItemType() != ItemType.LOST) {
            throw new IllegalArgumentException(
                    "Matching can only be performed for a LOST item"
            );
        }

        List<Item> foundItems = itemRepository.findByItemTypeAndStatus(
                ItemType.FOUND,
                ItemStatus.ACTIVE
        );

        return foundItems.stream()
                .filter(foundItem ->
                        !foundItem.getReportedBy().getId()
                                .equals(lostItem.getReportedBy().getId())
                )
                .map(foundItem ->
                        createMatchResponse(lostItem, foundItem)
                )
                .sorted(
                        Comparator.comparing(
                                MatchResponseDTO::getMatchScore
                        ).reversed()
                )
                .toList();
    }

    private MatchResponseDTO createMatchResponse(
            Item lostItem,
            Item foundItem) {

        int categoryScore =
                calculateCategoryScore(lostItem, foundItem);

        int colorScore =
                calculateColorScore(lostItem, foundItem);

        int locationScore =
                calculateLocationScore(lostItem, foundItem);

        int dateScore =
                calculateDateScore(lostItem, foundItem);

        int descriptionScore =
                calculateDescriptionScore(lostItem, foundItem);

        int totalScore =
                categoryScore
                + colorScore
                + locationScore
                + dateScore
                + descriptionScore;

        return new MatchResponseDTO(
                foundItem.getId(),
                foundItem.getTitle(),
                foundItem.getItemType(),
                foundItem.getCategory().getId(),
                foundItem.getCategory().getName(),
                foundItem.getColor(),
                foundItem.getLocation(),
                categoryScore,
                colorScore,
                locationScore,
                dateScore,
                descriptionScore,
                totalScore
        );
    }

    private int calculateCategoryScore(Item lostItem, Item foundItem) {

        if (lostItem.getCategory().getId()
                .equals(foundItem.getCategory().getId())) {

            return 30;
        }

        return 0;
    }

    private int calculateColorScore(Item lostItem, Item foundItem) {

        if (lostItem.getColor()
                .equalsIgnoreCase(foundItem.getColor())) {

            return 20;
        }

        return 0;
    }

    private int calculateLocationScore(Item lostItem, Item foundItem) {

        String lostLocation =
                lostItem.getLocation().trim().toLowerCase();

        String foundLocation =
                foundItem.getLocation().trim().toLowerCase();

        if (lostLocation.contains(foundLocation)
                || foundLocation.contains(lostLocation)) {

            return 25;
        }

        return 0;
    }

    private int calculateDateScore(Item lostItem, Item foundItem) {

        LocalDate lostDate = lostItem.getEventDate();
        LocalDate foundDate = foundItem.getEventDate();

        long difference = Math.abs(
                ChronoUnit.DAYS.between(lostDate, foundDate)
        );

        if (difference <= 1) {
            return 15;
        }

        if (difference == 2) {
            return 10;
        }

        if (difference == 3) {
            return 5;
        }

        return 0;
    }

    private int calculateDescriptionScore(
            Item lostItem,
            Item foundItem) {

        String[] lostWords = lostItem.getDescription()
                .toLowerCase()
                .split("\\s+");

        String foundDescription = foundItem.getDescription()
                .toLowerCase();

        int matchingWords = 0;

        for (String word : lostWords) {

            String cleanedWord =
                    word.replaceAll("[^a-zA-Z0-9]", "");

            if (!cleanedWord.isBlank()
                    && foundDescription.contains(cleanedWord)) {

                matchingWords++;
            }
        }

        if (lostWords.length == 0) {
            return 0;
        }

        double percentage =
                (double) matchingWords / lostWords.length;

        return (int) Math.round(percentage * 10);
    }
}