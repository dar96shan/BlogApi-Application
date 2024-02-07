package com.kk.blog.controllers;

import com.kk.blog.payloads.ApiResponse;
import com.kk.blog.payloads.CategoryDto;
import com.kk.blog.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    public CategoryService categoryService;

    //create
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
        CategoryDto created = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto
            ,@PathVariable Integer categoryId){
        CategoryDto updated = categoryService.updateCategory(categoryDto, categoryId);
        return ResponseEntity.ok(updated);
    }

    //delete
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable("id") Integer categoryId){
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(new ApiResponse("Category is deleted",true),HttpStatus.OK);
    }

    //get
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> findCategoryById(@PathVariable Integer id){
        CategoryDto categoryById = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryById);
    }

    //get all
    @GetMapping("/all")
    public ResponseEntity<List<CategoryDto>> findAllCategory(){
        List<CategoryDto> allCategory = categoryService.getAllCategory();
        return ResponseEntity.ok(allCategory);
    }

}
