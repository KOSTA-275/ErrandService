package com.dowadream.errand_service.repository;

import com.dowadream.errand_service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 리뷰 엔티티에 대한 데이터 액세스를 처리하는 리포지토리 인터페이스
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * 특정 심부름에 대한 모든 리뷰를 조회합니다.
     * @param errandSeq 심부름 일련번호
     * @return 해당 심부름의 리뷰 목록
     */
    List<Review> findByErrandErrandSeq(Long errandSeq);

    /**
     * 특정 서비스 제공에 대한 모든 리뷰를 조회합니다.
     * @param serviceOfferingId 서비스 제공 ID
     * @return 해당 서비스 제공의 리뷰 목록
     */
    List<Review> findByServiceOfferingId(Long serviceOfferingId);
}