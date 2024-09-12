package com.dowadream.errand_service.controller;

import com.dowadream.errand_service.dto.ReviewDTO;
import com.dowadream.errand_service.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 리뷰 관련 HTTP 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequestMapping("/ErrandService/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    /**
     * ReviewController 생성자
     * @param reviewService 리뷰 서비스 인스턴스
     */
    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * 새로운 리뷰를 생성합니다.
     * @param reviewDTO 리뷰 DTO
     * @return 생성된 리뷰 DTO와 HTTP 상태
     */
    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO) {
        ReviewDTO createdReview = reviewService.createReview(reviewDTO);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    /**
     * 특정 심부름에 대한 모든 리뷰를 조회합니다.
     * @param errandId 심부름 ID
     * @return 리뷰 DTO 목록과 HTTP 상태
     */
    @GetMapping("/errand/{errandId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByErrandId(@PathVariable Long errandId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByErrandId(errandId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * 특정 서비스 제공에 대한 모든 리뷰를 조회합니다.
     * @param serviceOfferingId 서비스 제공 ID
     * @return 리뷰 DTO 목록과 HTTP 상태
     */
    @GetMapping("/service-offering/{serviceOfferingId}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByServiceOfferingId(@PathVariable Long serviceOfferingId) {
        List<ReviewDTO> reviews = reviewService.getReviewsByServiceOfferingId(serviceOfferingId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    /**
     * 특정 리뷰를 ID로 조회합니다.
     * @param id 리뷰 ID
     * @return 리뷰 DTO와 HTTP 상태
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        ReviewDTO review = reviewService.getReviewById(id);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    /**
     * 특정 리뷰를 업데이트합니다.
     * @param id 리뷰 ID
     * @param reviewDTO 업데이트할 리뷰 정보
     * @return 업데이트된 리뷰 DTO와 HTTP 상태
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO) {
        ReviewDTO updatedReview = reviewService.updateReview(id, reviewDTO);
        return new ResponseEntity<>(updatedReview, HttpStatus.OK);
    }

    /**
     * 특정 리뷰를 삭제합니다.
     * @param id 삭제할 리뷰 ID
     * @return HTTP 상태
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}