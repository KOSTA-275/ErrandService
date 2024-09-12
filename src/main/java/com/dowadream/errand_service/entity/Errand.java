package com.dowadream.errand_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 심부름 엔티티 클래스
 */
@Entity
@Table(name = "errands")
@Data
public class Errand {
    /**
     * 심부름 고유 번호
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "errand_seq")
    @SequenceGenerator(name = "errand_seq", sequenceName = "errand_seq", allocationSize = 1)
    private Long errandSeq;

    /**
     * 심부름 제목
     */
    @Column(nullable = false, length = 100)
    private String title;

    /**
     * 심부름 설명
     */
    @Column(nullable = false, length = 500)
    private String description;

    /**
     * 요청자 고유 번호
     */
    @Column(name = "requester_seq")
    private Long requesterSeq;

    /**
     * 수행자 고유 번호
     */
    @Column(name = "runner_seq")
    private Long runnerSeq;

    /**
     * 요청자 닉네임
     */
    @Column(name = "requester_nickname")
    private String requesterNickname;

    /**
     * 수행자 닉네임
     */
    @Column(name = "runner_nickname")
    private String runnerNickname;

    /**
     * 심부름 상태 열거형
     */
    public enum ErrandStatus {
        REQUESTED, IN_PROGRESS, COMPLETED, CANCELLED
    }

    /**
     * 심부름 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ErrandStatus status = ErrandStatus.REQUESTED;

    /**
     * 생성 일시
     */
    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    /**
     * 수정 일시
     */
    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    /**
     * 심부름 카테고리
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * 심부름 이미지 목록
     */
    @OneToMany(mappedBy = "errand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    /**
     * 위치
     */
    @Column(nullable = false)
    private String location;

    /**
     * 가격
     */
    @Column(nullable = false)
    private BigDecimal price;

    /**
     * 예상 소요 시간 (분)
     */
    @Column(nullable = false)
    private Integer estimatedTime;

    /**
     * 마감 기한
     */
    @Column(nullable = false)
    private LocalDateTime deadline;

    /**
     * 심부름 리뷰 목록
     */
    @OneToMany(mappedBy = "errand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    /**
     * 이미지 추가
     * @param image 추가할 이미지
     */
    public void addImage(Image image) {
        images.add(image);
        image.setErrand(this);
        image.setImageType(Image.ImageType.ERRAND_REQUEST);
    }

    /**
     * 이미지 제거
     * @param image 제거할 이미지
     */
    public void removeImage(Image image) {
        images.remove(image);
        image.setErrand(null);
    }

    /**
     * 리뷰 추가
     * @param review 추가할 리뷰
     */
    public void addReview(Review review) {
        reviews.add(review);
        review.setErrand(this);
    }

    /**
     * 리뷰 제거
     * @param review 제거할 리뷰
     */
    public void removeReview(Review review) {
        reviews.remove(review);
        review.setErrand(null);
    }
}