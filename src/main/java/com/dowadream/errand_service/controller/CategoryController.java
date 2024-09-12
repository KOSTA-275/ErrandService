package com.dowadream.errand_service.controller;

import com.dowadream.errand_service.dto.CategoryDTO;
import com.dowadream.errand_service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * 카테고리 관련 HTTP 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequestMapping("/ErrandService/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * CategoryController 생성자
     * @param categoryService 카테고리 서비스 인스턴스
     */
    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 모든 카테고리 목록을 조회합니다.
     * @return 카테고리 DTO 목록
     */
    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    /**
     * 특정 ID의 카테고리를 조회합니다.
     * @param id 카테고리 ID
     * @return 카테고리 DTO와 HTTP 상태
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 새로운 카테고리를 생성합니다.
     * @param categoryDTO 카테고리 DTO
     * @return 생성된 카테고리 DTO와 HTTP 상태
     */
    @PostMapping
    public ResponseEntity<CategoryDTO> createCategory(@ModelAttribute CategoryDTO categoryDTO) {
        try {
            CategoryDTO createdCategory = categoryService.createCategory(categoryDTO);
            return ResponseEntity.ok(createdCategory);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 기존 카테고리를 수정합니다.
     * @param id 수정할 카테고리 ID
     * @param categoryDTO 수정할 카테고리 정보
     * @return 수정된 카테고리 DTO와 HTTP 상태
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @ModelAttribute CategoryDTO categoryDTO) {
        try {
            CategoryDTO updatedCategory = categoryService.updateCategory(id, categoryDTO);
            return ResponseEntity.ok(updatedCategory);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 카테고리를 삭제합니다.
     * @param id 삭제할 카테고리 ID
     * @return HTTP 상태
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}