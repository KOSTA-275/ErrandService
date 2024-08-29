package com.dowadream.errand_service.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ServiceOfferingDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal priceRange;
    private String location;
    private Long categoryId;
    private Long providerId;
    private List<MultipartFile> images;
    private LocalDateTime createdDate;
    private Integer completedTasks;
    private Double averageRating;

    // 추가 필드: 이미지 경로 목록 (조회 시 사용)
    private List<String> imagePaths;

    // MultipartFile은 JSON 직렬화가 불가능하므로, 이미지 업로드 시에만 사용
    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    // 이미지 경로 목록 설정 (조회 시 사용)
    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}