package com.dowadream.errand_service.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 서비스 제공 정보를 전송하기 위한 데이터 전송 객체(DTO)
 */
@Data
public class ServiceOfferingDTO {
    /**
     * 서비스 제공 ID
     */
    private Long id;

    /**
     * 서비스 제공 제목
     */
    private String title;

    /**
     * 서비스 제공 설명
     */
    private String description;

    /**
     * 가격 범위
     */
    private BigDecimal priceRange;

    /**
     * 위치
     */
    private String location;

    /**
     * 카테고리 ID
     */
    private Long categoryId;

    /**
     * 제공자 ID
     */
    private Long providerId;

    /**
     * 이미지 파일 목록
     */
    private List<MultipartFile> images;

    /**
     * 생성 일시
     */
    private LocalDateTime createdDate;

    /**
     * 완료된 작업 수
     */
    private Integer completedTasks;

    /**
     * 평균 평점
     */
    private Double averageRating;

    /**
     * 이미지 경로 목록 (조회 시 사용)
     */
    private List<String> imagePaths;

    /**
     * 이미지 파일 목록 설정
     * @param images 이미지 파일 목록
     */
    public void setImages(List<MultipartFile> images) {
        this.images = images;
    }

    /**
     * 이미지 경로 목록 설정 (조회 시 사용)
     * @param imagePaths 이미지 경로 목록
     */
    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }
}