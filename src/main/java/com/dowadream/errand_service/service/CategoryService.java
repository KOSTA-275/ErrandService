package com.dowadream.errand_service.service;

import com.dowadream.errand_service.entity.Category;
import com.dowadream.errand_service.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 카테고리 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 모든 카테고리를 조회합니다.
     * @return 전체 카테고리 목록
     */
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * 특정 ID의 카테고리를 조회합니다.
     * @param id 카테고리 ID
     * @return 조회된 카테고리 (Optional)
     */
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /**
     * 새로운 카테고리를 생성합니다.
     * @param category 생성할 카테고리 정보
     * @return 생성된 카테고리
     */
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    /**
     * 기존 카테고리를 수정합니다.
     * @param id 수정할 카테고리 ID
     * @param categoryDetails 수정할 카테고리 정보
     * @return 수정된 카테고리
     */
    public Category updateCategory(Long id, Category categoryDetails) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다"));

        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());
        category.setParentCategory(categoryDetails.getParentCategory());

        return categoryRepository.save(category);
    }

    /**
     * 특정 카테고리를 삭제합니다.
     * @param id 삭제할 카테고리 ID
     */
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * 최상위 카테고리 목록을 조회합니다.
     * @return 최상위 카테고리 목록
     */
    public List<Category> getRootCategories() {
        return categoryRepository.findAll().stream()
                .filter(category -> category.getParentCategory() == null)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리 트리 구조를 조회합니다.
     * @return 카테고리 트리 구조
     */
    public List<Category> getCategoryTree() {
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories.stream()
                .filter(category -> category.getParentCategory() == null)
                .map(this::setCategoryHierarchy)
                .collect(Collectors.toList());
    }

    /**
     * 카테고리의 계층 구조를 설정합니다.
     * @param category 설정할 카테고리
     * @return 계층 구조가 설정된 카테고리
     */
    private Category setCategoryHierarchy(Category category) {
        List<Category> children = category.getSubCategories();
        if (children != null && !children.isEmpty()) {
            category.setSubCategories(
                    children.stream()
                            .map(this::setCategoryHierarchy)
                            .collect(Collectors.toList())
            );
        }
        return category;
    }
}