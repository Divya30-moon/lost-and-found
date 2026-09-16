package com.jsp.lostAndFound.dto;

import java.time.LocalDate;

import com.jsp.lostAndFound.entity.ItemStatus;
import com.jsp.lostAndFound.entity.ItemType;

public class ItemSearchRequestDTO {

    private String keyword;
    private Long categoryId;
    private String location;
    private ItemType itemType;
    private ItemStatus status;
    private String color;
    private LocalDate fromDate;
    private LocalDate toDate;

    public ItemSearchRequestDTO() {
    }

    public ItemSearchRequestDTO(String keyword, Long categoryId,
                                String location, ItemType itemType,
                                ItemStatus status, String color,
                                LocalDate fromDate, LocalDate toDate) {

        this.keyword = keyword;
        this.categoryId = categoryId;
        this.location = location;
        this.itemType = itemType;
        this.status = status;
        this.color = color;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public ItemStatus getStatus() {
        return status;
    }

    public void setStatus(ItemStatus status) {
        this.status = status;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }
}