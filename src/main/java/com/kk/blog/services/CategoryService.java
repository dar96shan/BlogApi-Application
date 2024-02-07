package com.kk.blog.services;

import com.kk.blog.payloads.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto, Integer id);

    void deleteCategory(Integer id);

    List<CategoryDto> getAllCategory();

    CategoryDto getCategoryById( Integer id);
}
