package com.jsp.lostAndFound.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.jsp.lostAndFound.entity.Category;
import com.jsp.lostAndFound.exception.CategoryAlreadyExistsException;
import com.jsp.lostAndFound.exception.CategoryNotFoundException;
import com.jsp.lostAndFound.repository.CategoryRepository;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category createCategory(Category category) {

        if (categoryRepository.existsByNameIgnoreCase(category.getName())) {
            throw new CategoryAlreadyExistsException(
                    "Category already exists: " + category.getName()
            );
        }

        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {

        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + id
                ));
    }

    @Override
    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(Long id, Category category) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + id
                ));

        String newName = category.getName();

        if (!existingCategory.getName().equalsIgnoreCase(newName)
                && categoryRepository.existsByNameIgnoreCase(newName)) {

            throw new CategoryAlreadyExistsException(
                    "Category already exists: " + newName
            );
        }

        existingCategory.setName(newName);

        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {

        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Category not found with id: " + id
                ));

        categoryRepository.delete(existingCategory);
    }
}