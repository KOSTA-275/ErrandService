package com.dowadream.errand_service.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CategoryDTO {
    private Long categoryId;
    private String name;
    private String description;
    private Long parentCategoryId;
    private List<Long> subCategoryIds;
    private MultipartFile image;
    private String imagePath; // 이미지 조회 시 사용
}