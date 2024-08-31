package com.dowadream.errand_service.service;

import com.dowadream.errand_service.dto.ReviewDTO;
import com.dowadream.errand_service.entity.Errand;
import com.dowadream.errand_service.entity.Review;
import com.dowadream.errand_service.entity.ServiceOffering;
import com.dowadream.errand_service.exception.ResourceNotFoundException;
import com.dowadream.errand_service.repository.ErrandRepository;
import com.dowadream.errand_service.repository.ReviewRepository;
import com.dowadream.errand_service.repository.ServiceOfferingRepository;
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
    private final ServiceOfferingRepository serviceOfferingRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ErrandRepository errandRepository, ServiceOfferingRepository serviceOfferingRepository) {
        this.reviewRepository = reviewRepository;
        this.errandRepository = errandRepository;
        this.serviceOfferingRepository = serviceOfferingRepository;
    }

    public ReviewDTO createReview(ReviewDTO reviewDTO) {
        Review review = new Review();
        updateReviewFromDTO(review, reviewDTO);
        return convertToDTO(reviewRepository.save(review));
    }

    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
        updateReviewFromDTO(review, reviewDTO);
        return convertToDTO(reviewRepository.save(review));
    }

    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));

        if (review.getErrand() != null) {
            review.getErrand().removeReview(review);
        }
        if (review.getServiceOffering() != null) {
            review.getServiceOffering().removeReview(review);
        }

        reviewRepository.delete(review);
    }

    public List<ReviewDTO> getReviewsByErrandId(Long errandId) {
        return reviewRepository.findByErrandErrandSeq(errandId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ReviewDTO> getReviewsByServiceOfferingId(Long serviceOfferingId) {
        return reviewRepository.findByServiceOfferingId(serviceOfferingId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO getReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
    }

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

    private void updateReviewFromDTO(Review review, ReviewDTO dto) {
        review.setReviewerId(dto.getReviewerId());
        review.setRating(dto.getRating());
        review.setComments(dto.getComments());

        if (dto.getErrandId() != null) {
            Errand errand = errandRepository.findById(dto.getErrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Errand not found with id: " + dto.getErrandId()));
            review.setErrand(errand);
        } else {
            review.setErrand(null);
        }

        if (dto.getServiceOfferingId() != null) {
            ServiceOffering serviceOffering = serviceOfferingRepository.findById(dto.getServiceOfferingId())
                    .orElseThrow(() -> new ResourceNotFoundException("ServiceOffering not found with id: " + dto.getServiceOfferingId()));
            review.setServiceOffering(serviceOffering);
        } else {
            review.setServiceOffering(null);
        }
    }
}