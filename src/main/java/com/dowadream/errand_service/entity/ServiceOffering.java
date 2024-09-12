package com.dowadream.errand_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 서비스 제공 엔티티 클래스
 */
@Entity
@Table(name = "service_offerings")
@Getter
@Setter
public class ServiceOffering {
    /**
     * 서비스 제공 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_offering_seq")
    @SequenceGenerator(name = "service_offering_seq", sequenceName = "service_offering_seq", allocationSize = 1)
    private Long id;

    /**
     * 서비스 제공 제목
     */
    @Column(nullable = false)
    private String title;

    /**
     * 서비스 제공 설명
     */
    @Column(length = 500)
    private String description;

    /**
     * 가격 범위
     */
    @Column(nullable = false)
    private BigDecimal priceRange;

    /**
     * 위치
     */
    @Column(nullable = false)
    private String location;

    /**
     * 서비스 제공 카테고리
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * 제공자 ID
     */
    @Column(nullable = false)
    private Long providerId;

    /**
     * 서비스 제공 이미지 목록
     */
    @OneToMany(mappedBy = "serviceOffering", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    /**
     * 생성 일시
     */
    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    /**
     * 서비스 제공 리뷰 목록
     */
    @OneToMany(mappedBy = "serviceOffering", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    /**
     * 완료된 작업 수
     */
    @Column(nullable = false)
    private Integer completedTasks = 0;

    /**
     * 이미지 추가
     * @param image 추가할 이미지
     */
    public void addImage(Image image) {
        images.add(image);
        image.setServiceOffering(this);
        image.setImageType(Image.ImageType.SERVICE_OFFERING);
    }

    /**
     * 이미지 제거
     * @param image 제거할 이미지
     */
    public void removeImage(Image image) {
        images.remove(image);
        image.setServiceOffering(null);
    }

    /**
     * 리뷰 추가
     * @param review 추가할 리뷰
     */
    public void addReview(Review review) {
        reviews.add(review);
        review.setServiceOffering(this);
    }

    /**
     * 리뷰 제거
     * @param review 제거할 리뷰
     */
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setServiceOffering(null);
    }

    /**
     * 완료된 작업 수 증가
     */
    public void incrementCompletedTasks() {
        this.completedTasks++;
    }
}