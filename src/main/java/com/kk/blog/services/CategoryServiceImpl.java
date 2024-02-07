package com.kk.blog.services;

import com.kk.blog.entities.Category;
import com.kk.blog.exceptions.ResourceNotFoundException;
import com.kk.blog.payloads.CategoryDto;
import com.kk.blog.respositories.CategoryRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    public CategoryRepo categoryRepo;

    @Autowired
    public ModelMapper modelMapper;


    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = this.catDtoToCat(categoryDto);
        Category createCategory = categoryRepo.save(category);
        return this.catToCatDto(createCategory);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer id) {
        Category upCategory = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", id));
        upCategory.setCategoryTitle(categoryDto.getCategoryTitle());
        upCategory.setCategoryDescription(categoryDto.getCategoryDescription());
        Category saveCategory = categoryRepo.save(upCategory);

        return this.modelMapper.map(saveCategory,CategoryDto.class);
        //return this.catToCatDto(saveCategory);
    }

    @Override
    public void deleteCategory( Integer id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", id));
        categoryRepo.delete(category);

    }

    @Override
    public List<CategoryDto> getAllCategory() {
        List<Category> allCategory = categoryRepo.findAll();
        List<CategoryDto> collect = allCategory.stream()
                .map(this::catToCatDto)
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public CategoryDto getCategoryById( Integer id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "Category id", id));
        CategoryDto categoryById = this.catToCatDto(category);
        return categoryById;
    }


    public Category catDtoToCat(CategoryDto categoryDto){
        return modelMapper.map(categoryDto, Category.class);

    }

    public CategoryDto catToCatDto(Category category){
       return modelMapper.map(category,CategoryDto.class);
    }
}
