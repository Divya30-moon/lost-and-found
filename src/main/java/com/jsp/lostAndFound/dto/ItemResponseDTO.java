package com.jsp.lostAndFound.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.jsp.lostAndFound.entity.ItemStatus;
import com.jsp.lostAndFound.entity.ItemType;

public class ItemResponseDTO {

    private Long id;
    private String title;
    private String description;
    private ItemType itemType;

    private Long categoryId;
    private String categoryName;

    private String color;
    private String location;
    private LocalDate eventDate;
    private ItemStatus status;

    private Long reportedById;
    private String reportedByName;

    private LocalDateTime createdAt;

    public ItemResponseDTO() {
    }

    public ItemResponseDTO(Long id, String title, String description,
                           ItemType itemType, Long categoryId,
                           String categoryName, String color,
                           String location, LocalDate eventDate,
                           ItemStatus status, Long reportedById,
                           String reportedByName, LocalDateTime createdAt) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.itemType = itemType;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.color = color;
        this.location = location;
        this.eventDate = eventDate;
        this.status = status;
        this.reportedById = reportedById;
        this.reportedByName = reportedByName;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public Long getReportedById() {
        return reportedById;
    }

    public void setReportedById(Long reportedById) {
        this.reportedById = reportedById;
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}