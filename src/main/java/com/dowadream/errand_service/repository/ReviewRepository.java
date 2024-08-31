package com.dowadream.errand_service.repository;

import com.dowadream.errand_service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByErrandErrandSeq(Long errandSeq);
    List<Review> findByServiceOfferingId(Long serviceOfferingId);
}