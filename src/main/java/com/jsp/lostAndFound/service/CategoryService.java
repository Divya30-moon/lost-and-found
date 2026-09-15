package com.jsp.lostAndFound.service;

import java.util.List;

import com.jsp.lostAndFound.entity.Category;

public interface CategoryService {

    Category createCategory(Category category);

    Category getCategoryById(Long id);

    List<Category> getAllCategories();

    Category updateCategory(Long id, Category category);

    void deleteCategory(Long id);
}