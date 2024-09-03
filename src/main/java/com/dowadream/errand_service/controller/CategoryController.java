package com.dowadream.errand_service.controller;

import com.dowadream.errand_service.dto.CategoryDTO;
import com.dowadream.errand_service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ErrandService/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@ModelAttribute CategoryDTO categoryDTO) {
        try {
            CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok(createdCategory);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @ModelAttribute CategoryDTO categoryDTO) {
        try {
            CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(updatedCategory);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

}