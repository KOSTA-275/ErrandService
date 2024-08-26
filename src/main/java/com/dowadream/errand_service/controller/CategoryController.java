package com.dowadream.errand_service.controller;

import com.dowadream.errand_service.entity.Category;
import com.dowadream.errand_service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 카테고리 관련 API 엔드포인트를 제공하는 컨트롤러 클래스입니다.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 모든 카테고리를 조회합니다.
     * @return 전체 카테고리 목록
     */
    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * 특정 ID의 카테고리를 조회합니다.
     * @param id 카테고리 ID
     * @return 조회된 카테고리 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 새로운 카테고리를 생성합니다.
     * @param category 생성할 카테고리 정보
     * @return 생성된 카테고리
     */
    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    /**
     * 기존 카테고리를 수정합니다.
     * @param id 수정할 카테고리 ID
     * @param categoryDetails 수정할 카테고리 정보
     * @return 수정된 카테고리
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category categoryDetails) {
        Category updatedCategory = categoryService.updateCategory(id, categoryDetails);
        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * 특정 카테고리를 삭제합니다.
     * @param id 삭제할 카테고리 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리 트리 구조를 조회합니다.
     * @return 카테고리 트리 구조
     */
    @GetMapping("/tree")
    public List<Category> getCategoryTree() {
        return categoryService.getCategoryTree();
    }
}