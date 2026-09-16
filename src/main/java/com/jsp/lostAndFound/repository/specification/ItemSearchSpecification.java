package com.jsp.lostAndFound.repository.specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.jsp.lostAndFound.dto.ItemSearchRequestDTO;
import com.jsp.lostAndFound.entity.Item;

import jakarta.persistence.criteria.Predicate;

public class ItemSearchSpecification {

    public static Specification<Item> filterBy(ItemSearchRequestDTO request) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // Keyword search in title or description
            if (request.getKeyword() != null && !request.getKeyword().isBlank()) {

                String keyword = "%" + request.getKeyword().trim().toLowerCase() + "%";

                Predicate titleMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        keyword
                );

                Predicate descriptionMatch = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")),
                        keyword
                );

                predicates.add(criteriaBuilder.or(titleMatch, descriptionMatch));
            }

            // Category filter
            if (request.getCategoryId() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("category").get("id"),
                                request.getCategoryId()
                        )
                );
            }

            // Location filter
            if (request.getLocation() != null && !request.getLocation().isBlank()) {

                String location = "%" + request.getLocation().trim().toLowerCase() + "%";

                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("location")),
                                location
                        )
                );
            }

            // Item type filter
            if (request.getItemType() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("itemType"),
                                request.getItemType()
                        )
                );
            }

            // Status filter
            if (request.getStatus() != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("status"),
                                request.getStatus()
                        )
                );
            }

            // Color filter
            if (request.getColor() != null && !request.getColor().isBlank()) {

                String color = "%" + request.getColor().trim().toLowerCase() + "%";

                predicates.add(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("color")),
                                color
                        )
                );
            }

            // From date filter
            if (request.getFromDate() != null) {

                LocalDate fromDate = request.getFromDate();

                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(
                                root.<LocalDate>get("eventDate"),
                                fromDate
                        )
                );
            }

            // To date filter
            if (request.getToDate() != null) {

                LocalDate toDate = request.getToDate();

                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(
                                root.<LocalDate>get("eventDate"),
                                toDate
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0])
            );
        };
    }
}