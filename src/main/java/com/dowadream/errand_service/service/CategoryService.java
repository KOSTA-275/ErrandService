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

@Service
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ImageRepository imageRepository, FileStorageService fileStorageService) {
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDTO> getCategoryById(Long id) {
        return categoryRepository.findById(id).map(this::convertToDTO);
    }

    public CategoryDTO createCategory(CategoryDTO categoryDTO) throws IOException {
        Category category = convertToEntity(categoryDTO);
        if (categoryDTO.getImage() != null && !categoryDTO.getImage().isEmpty()) {
            Image image = uploadImage(categoryDTO.getImage());
            category.setImage(image);
        }
        return convertToDTO(categoryRepository.save(category));
    }

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

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

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

    private Category convertToEntity(CategoryDTO dto) {
        Category category = new Category();
        updateCategoryFromDTO(category, dto);
        return category;
    }

    private void updateCategoryFromDTO(Category category, CategoryDTO dto) {
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
    }
}