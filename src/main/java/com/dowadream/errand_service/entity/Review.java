package com.dowadream.errand_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

/**
 * 리뷰 엔티티 클래스
 */
@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review {
    /**
     * 리뷰 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
    @SequenceGenerator(name = "review_seq", sequenceName = "review_seq", allocationSize = 1)
    private Long id;

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
     * 리뷰 작성자 ID
     */
    @Column(nullable = false)
    private Long reviewerId;

    /**
     * 평점
     */
    @Column(nullable = false)
    private Integer rating;

    /**
     * 리뷰 내용
     */
    @Column(length = 500)
    private String comments;

    /**
     * 생성 일시
     */
    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    /**
     * 심부름 설정
     * @param errand 설정할 심부름
     */
    public void setErrand(Errand errand) {
        this.errand = errand;
        if (errand != null) {
            errand.getReviews().add(this);
        }
    }

    /**
     * 서비스 제공 설정
     * @param serviceOffering 설정할 서비스 제공
     */
    public void setServiceOffering(ServiceOffering serviceOffering) {
        this.serviceOffering = serviceOffering;
        if (serviceOffering != null) {
            serviceOffering.getReviews().add(this);
        }
    }
}