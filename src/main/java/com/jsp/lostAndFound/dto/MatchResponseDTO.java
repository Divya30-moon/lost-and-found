package com.jsp.lostAndFound.dto;

import com.jsp.lostAndFound.entity.ItemType;

public class MatchResponseDTO {

    private Long itemId;
    private String title;
    private ItemType itemType;
    private Long categoryId;
    private String categoryName;
    private String color;
    private String location;

    private Integer categoryScore;
    private Integer colorScore;
    private Integer locationScore;
    private Integer dateScore;
    private Integer descriptionScore;

    private Integer matchScore;

    public MatchResponseDTO() {
    }

    public MatchResponseDTO(Long itemId, String title, ItemType itemType,
                            Long categoryId, String categoryName,
                            String color, String location,
                            Integer categoryScore, Integer colorScore,
                            Integer locationScore, Integer dateScore,
                            Integer descriptionScore, Integer matchScore) {

        this.itemId = itemId;
        this.title = title;
        this.itemType = itemType;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.color = color;
        this.location = location;
        this.categoryScore = categoryScore;
        this.colorScore = colorScore;
        this.locationScore = locationScore;
        this.dateScore = dateScore;
        this.descriptionScore = descriptionScore;
        this.matchScore = matchScore;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getCategoryScore() {
        return categoryScore;
    }

    public void setCategoryScore(Integer categoryScore) {
        this.categoryScore = categoryScore;
    }

    public Integer getColorScore() {
        return colorScore;
    }

    public void setColorScore(Integer colorScore) {
        this.colorScore = colorScore;
    }

    public Integer getLocationScore() {
        return locationScore;
    }

    public void setLocationScore(Integer locationScore) {
        this.locationScore = locationScore;
    }

    public Integer getDateScore() {
        return dateScore;
    }

    public void setDateScore(Integer dateScore) {
        this.dateScore = dateScore;
    }

    public Integer getDescriptionScore() {
        return descriptionScore;
    }

    public void setDescriptionScore(Integer descriptionScore) {
        this.descriptionScore = descriptionScore;
    }

    public Integer getMatchScore() {
        return matchScore;
    }

    public void setMatchScore(Integer matchScore) {
        this.matchScore = matchScore;
    }
}