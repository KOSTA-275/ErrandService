package com.dowadream.errand_service.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 카테고리 정보를 전송하기 위한 데이터 전송 객체(DTO)
 */
@Data
public class CategoryDTO {
    /**
     * 카테고리 ID
     */
    private Long categoryId;

    /**
     * 카테고리 이름
     */
    private String name;

    /**
     * 카테고리 설명
     */
    private String description;

    /**
     * 카테고리 이미지 파일
     */
    private MultipartFile image;

    /**
     * 카테고리 이미지 경로 (이미지 조회 시 사용)
     */
    private String imagePath;
}