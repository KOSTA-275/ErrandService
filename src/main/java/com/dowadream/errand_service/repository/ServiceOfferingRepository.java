package com.dowadream.errand_service.repository;

import com.dowadream.errand_service.entity.ServiceOffering;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {
    Page<ServiceOffering> findByCategoryCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT so FROM ServiceOffering so LEFT JOIN so.reviews r WHERE " +
            "(:location IS NULL OR so.location = :location) AND " +
            "(:categoryId IS NULL OR so.category.categoryId = :categoryId) " +
            "GROUP BY so " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'latest' THEN so.createdDate END DESC, " +
            "CASE WHEN :sortBy = 'highestRating' THEN AVG(r.rating) END DESC, " +
            "CASE WHEN :sortBy = 'mostTasks' THEN COUNT(so.completedTasks) END DESC")
    Page<ServiceOffering> findServiceOfferingsByFilters(
            @Param("location") String location,
            @Param("categoryId") Long categoryId,
            @Param("sortBy") String sortBy,
            Pageable pageable
    );
}