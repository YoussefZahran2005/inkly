package com.zahran.blog_platform.service.impl;

import com.zahran.blog_platform.domain.entity.Category;
import com.zahran.blog_platform.repository.CategoryRepository;
import com.zahran.blog_platform.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    final private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }
}
