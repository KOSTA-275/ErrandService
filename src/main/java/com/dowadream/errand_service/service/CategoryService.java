package com.dowadream.errand_service.service;

import com.dowadream.errand_service.dto.CategoryDTO;
import com.dowadream.errand_service.entity.Category;
import com.dowadream.errand_service.entity.Image;
import com.dowadream.errand_service.repository.CategoryRepository;
import com.dowadream.errand_service.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 카테고리 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final FileStorageService fileStorageService;

    /**
     * CategoryService 생성자
     * @param categoryRepository 카테고리 리포지토리
     * @param imageRepository 이미지 리포지토리
     * @param fileStorageService 파일 저장 서비스
     */
    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ImageRepository imageRepository, FileStorageService fileStorageService) {
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * 모든 카테고리를 조회합니다.
     * @return 카테고리 DTO 목록
     */
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * ID로 카테고리를 조회합니다.
     * @param id 카테고리 ID
     * @return 카테고리 DTO (Optional)
     */
    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(this::convertToDTO);
    }

    /**
     * 새로운 카테고리를 생성합니다.
     * @param categoryDTO 카테고리 DTO
     * @return 생성된 카테고리 DTO
     * @throws IOException 이미지 저장 중 오류 발생 시
     */
    public CategoryDTO createCategory(CategoryDTO categoryDTO) throws IOException {
        Category category = convertToEntity(categoryDTO);
        if (categoryDTO.getImage() != null && !categoryDTO.getImage().isEmpty()) {
            Image image = uploadImage(categoryDTO.getImage());
            category.setImage(image);
        }
        return convertToDTO(categoryRepository.save(category));
    }

    /**
     * 기존 카테고리를 수정합니다.
     * @param id 수정할 카테고리 ID
     * @param categoryDTO 수정할 카테고리 정보
     * @return 수정된 카테고리 DTO
     * @throws IOException 이미지 저장 중 오류 발생 시
     */
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) throws IOException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        updateCategoryFromDTO(category, categoryDTO);
        if (categoryDTO.getImage() != null && !categoryDTO.getImage().isEmpty()) {
            Image image = uploadImage(categoryDTO.getImage());
            category.setImage(image);
        }
        return convertToDTO(categoryRepository.save(category));
    }

    /**
     * 카테고리를 삭제합니다.
     * @param id 삭제할 카테고리 ID
     */
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * 이미지를 업로드하고 이미지 엔티티를 생성합니다.
     * @param file 업로드할 이미지 파일
     * @return 생성된 이미지 엔티티
     * @throws IOException 이미지 저장 중 오류 발생 시
     */
    private Image uploadImage(MultipartFile file) throws IOException {
        String fileName = fileStorageService.storeFile(file);
        Image image = new Image();
        image.setFileName(fileName);
        image.setFilePath(fileStorageService.getFileStorageLocation().resolve(fileName).toString());
        image.setFileType(file.getContentType());
        image.setFileSize((int) file.getSize());
        image.setImageType(Image.ImageType.CATEGORY);
        return imageRepository.save(image);
    }

    /**
     * 카테고리 엔티티를 DTO로 변환합니다.
     * @param category 카테고리 엔티티
     * @return 카테고리 DTO
     */
    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        if (category.getImage() != null) {
            dto.setImagePath(category.getImage().getFilePath());
        }
        return dto;
    }

    /**
     * 카테고리 DTO를 엔티티로 변환합니다.
     * @param dto 카테고리 DTO
     * @return 카테고리 엔티티
     */
    private Category convertToEntity(CategoryDTO dto) {
        Category category = new Category();
        updateCategoryFromDTO(category, dto);
        return category;
    }

    /**
     * DTO의 정보로 카테고리 엔티티를 업데이트합니다.
     * @param category 업데이트할 카테고리 엔티티
     * @param dto 카테고리 DTO
     */
    private void updateCategoryFromDTO(Category category, CategoryDTO dto) {
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
    }
}