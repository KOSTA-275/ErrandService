package com.dowadream.errand_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 리뷰 정보를 전송하기 위한 데이터 전송 객체(DTO)
 */
@Data
public class ReviewDTO {
    /**
     * 리뷰 ID
     */
    private Long id;

    /**
     * 심부름 ID
     */
    private Long errandId;

    /**
     * 서비스 제공 ID
     */
    private Long serviceOfferingId;

    /**
     * 리뷰 작성자 ID
     */
    private Long reviewerId;

    /**
     * 평점
     */
    private Integer rating;

    /**
     * 리뷰 내용
     */
    private String comments;

    /**
     * 생성 일시
     */
    private LocalDateTime createdDate;

    /**
     * 이 리뷰가 심부름에 대한 것인지 확인
     * @return 심부름에 대한 리뷰이면 true, 아니면 false
     */
    public boolean isErrandReview() {
        return errandId != null;
    }

    /**
     * 이 리뷰가 서비스 제공에 대한 것인지 확인
     * @return 서비스 제공에 대한 리뷰이면 true, 아니면 false
     */
    public boolean isServiceOfferingReview() {
        return serviceOfferingId != null;
    }
}