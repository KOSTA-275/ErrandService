package com.dowadream.errand_service.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrandDTO {
    private Long errandSeq;
    private String title;
    private String description;
    private Long requesterSeq;
    private Long runnerSeq;
    private String status;
    private Long categoryId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private String location;
    private BigDecimal price;
    private Integer estimatedTime;
    private LocalDateTime deadline;
    private List<MultipartFile> images;
    private List<String> imagePaths;  // 이미지 경로 목록 (조회 시 사용)

    // MultipartFile은 JSON 직렬화가 불가능하므로, 이미지 업로드 시에만 사용
    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    // 이미지 경로 목록 설정 (조회 시 사용)
    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}