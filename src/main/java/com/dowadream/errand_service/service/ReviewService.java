package com.dowadream.errand_service.service;

import com.dowadream.errand_service.dto.ReviewDTO;
import com.dowadream.errand_service.entity.Errand;
import com.dowadream.errand_service.entity.Review;
import com.dowadream.errand_service.repository.ErrandRepository;
import com.dowadream.errand_service.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ErrandRepository errandRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ErrandRepository errandRepository) {
        this.reviewRepository = reviewRepository;
        this.errandRepository = errandRepository;
    }

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Errand errand = errandRepository.findById(reviewDTO.getErrandId())
                .orElseThrow(() -> new RuntimeException("Errand not found"));

        Review review = new Review();
        review.setErrand(errand);
        review.setReviewerId(reviewDTO.getReviewerId());
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        return convertToDTO(reviewRepository.save(review));
    }

    public List<ReviewDTO> getReviewsByErrandId(Long errandId) {
        return reviewRepository.findByErrandErrandSeq(errandId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setErrandId(review.getErrand().getErrandSeq());
        dto.setReviewerId(review.getReviewerId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedDate(review.getCreatedDate());
        return dto;
    }
}