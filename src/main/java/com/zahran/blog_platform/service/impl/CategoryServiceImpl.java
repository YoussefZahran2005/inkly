package com.zahran.blog_platform.service.impl;

import com.zahran.blog_platform.domain.entity.Category;
import com.zahran.blog_platform.repository.CategoryRepository;
import com.zahran.blog_platform.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    final private CategoryRepository categoryRepository;

    // there is an error in the terminal about "bean" creating.
    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        String categoryName = category.getName();
        if(categoryRepository.existsByNameIgnoreCase(categoryName)) {
            throw new IllegalArgumentException("Category already exists with name: " + categoryName);
        }
        return categoryRepository.save(category);
    }
}
