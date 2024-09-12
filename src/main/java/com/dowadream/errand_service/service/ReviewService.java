package com.dowadream.errand_service.service;

import com.dowadream.errand_service.dto.ReviewDTO;
import com.dowadream.errand_service.entity.Errand;
import com.dowadream.errand_service.entity.Review;
import com.dowadream.errand_service.entity.ServiceOffering;
import com.dowadream.errand_service.exception.BadRequestException;
import com.dowadream.errand_service.exception.ResourceNotFoundException;
import com.dowadream.errand_service.repository.ErrandRepository;
import com.dowadream.errand_service.repository.ReviewRepository;
import com.dowadream.errand_service.repository.ServiceOfferingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 리뷰 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ErrandRepository errandRepository;
    private final ServiceOfferingRepository serviceOfferingRepository;

    /**
     * ReviewService 생성자
     * @param reviewRepository 리뷰 리포지토리
     * @param errandRepository 심부름 리포지토리
     * @param serviceOfferingRepository 서비스 제공 리포지토리
     */
    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         ErrandRepository errandRepository,
                         ServiceOfferingRepository serviceOfferingRepository) {
        this.reviewRepository = reviewRepository;
        this.errandRepository = errandRepository;
        this.serviceOfferingRepository = serviceOfferingRepository;
    }

    /**
     * 새로운 리뷰를 생성합니다.
     * @param reviewDTO 리뷰 DTO
     * @return 생성된 리뷰 DTO
     */
    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        review.setReviewerId(reviewDTO.getReviewerId());
        review.setRating(reviewDTO.getRating());
        review.setComments(reviewDTO.getComments());

        if (reviewDTO.getErrandId() != null) {
            Errand errand = errandRepository.findById(reviewDTO.getErrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("심부름을 찾을 수 없습니다. ID: " + reviewDTO.getErrandId()));
            review.setErrand(errand);
        } else if (reviewDTO.getServiceOfferingId() != null) {
            ServiceOffering serviceOffering = serviceOfferingRepository.findById(reviewDTO.getServiceOfferingId())
                    .orElseThrow(() -> new ResourceNotFoundException("서비스 제공을 찾을 수 없습니다. ID: " + reviewDTO.getServiceOfferingId()));
            review.setServiceOffering(serviceOffering);
        } else {
            throw new BadRequestException("심부름 ID 또는 서비스 제공 ID 중 하나는 반드시 제공되어야 합니다.");
        }

        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

    /**
     * 특정 심부름에 대한 모든 리뷰를 조회합니다.
     * @param errandId 심부름 ID
     * @return 리뷰 DTO 목록
     */
    public List<ReviewDTO> getReviewsByErrandId(Long errandId) {
        List<Review> reviews = reviewRepository.findByErrandErrandSeq(errandId);
        return reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 특정 서비스 제공에 대한 모든 리뷰를 조회합니다.
     * @param serviceOfferingId 서비스 제공 ID
     * @return 리뷰 DTO 목록
     */
    public List<ReviewDTO> getReviewsByServiceOfferingId(Long serviceOfferingId) {
        List<Review> reviews = reviewRepository.findByServiceOfferingId(serviceOfferingId);
        return reviews.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 특정 리뷰를 ID로 조회합니다.
     * @param id 리뷰 ID
     * @return 리뷰 DTO
     */
    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("리뷰를 찾을 수 없습니다. ID: " + id));
        return convertToDTO(review);
    }

    /**
     * 특정 리뷰를 업데이트합니다.
     * @param id 리뷰 ID
     * @param reviewDTO 업데이트할 리뷰 정보
     * @return 업데이트된 리뷰 DTO
     */
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("리뷰를 찾을 수 없습니다. ID: " + id));

        review.setRating(reviewDTO.getRating());
        review.setComments(reviewDTO.getComments());

        Review updatedReview = reviewRepository.save(review);
        return convertToDTO(updatedReview);
    }

    /**
     * 특정 리뷰를 삭제합니다.
     * @param id 삭제할 리뷰 ID
     */
    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("리뷰를 찾을 수 없습니다. ID: " + id);
        }
        reviewRepository.deleteById(id);
    }

    /**
     * 리뷰 엔티티를 DTO로 변환합니다.
     * @param review 리뷰 엔티티
     * @return 리뷰 DTO
     */
    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setReviewerId(review.getReviewerId());
        dto.setRating(review.getRating());
        dto.setComments(review.getComments());
        dto.setCreatedDate(review.getCreatedDate());
        if (review.getErrand() != null) {
            dto.setErrandId(review.getErrand().getErrandSeq());
        }
        if (review.getServiceOffering() != null) {
            dto.setServiceOfferingId(review.getServiceOffering().getId());
        }
        return dto;
    }
}