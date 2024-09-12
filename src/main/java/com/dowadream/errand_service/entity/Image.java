package com.dowadream.errand_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 이미지 엔티티 클래스
 */
@Entity
@Table(name = "images")
@Data
public class Image {
    /**
     * 이미지 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "image_seq")
    @SequenceGenerator(name = "image_seq", sequenceName = "image_seq", allocationSize = 1)
    private Long imageId;

    /**
     * 파일 이름
     */
    @Column(nullable = false)
    private String fileName;

    /**
     * 파일 경로
     */
    @Column(nullable = false)
    private String filePath;

    /**
     * 파일 타입
     */
    private String fileType;

    /**
     * 파일 크기
     */
    private Integer fileSize;

    /**
     * 업로드 일시
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime uploadDate = LocalDateTime.now();

    /**
     * 이미지 타입
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "IMAGE_TYPE", nullable = false)
    private ImageType imageType;

    /**
     * 연관된 심부름
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "errand_seq")
    private Errand errand;

    /**
     * 연관된 서비스 제공
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_offering_id")
    private ServiceOffering serviceOffering;

    /**
     * 이미지 타입 열거형
     */
    public enum ImageType {
        ERRAND_REQUEST, SERVICE_OFFERING, CATEGORY
    }
}