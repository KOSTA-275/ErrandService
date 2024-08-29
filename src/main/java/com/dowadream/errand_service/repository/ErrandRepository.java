package com.dowadream.errand_service.repository;

import com.dowadream.errand_service.entity.Errand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrandRepository extends JpaRepository<Errand, Long> {
    @EntityGraph(attributePaths = {"category"})
    Page<Errand> findAll(Pageable pageable);

    Page<Errand> findByCategoryCategoryId(Long categoryId, Pageable pageable);

    @Query("SELECT e FROM Errand e WHERE " +
            "(:location IS NULL OR e.location = :location) AND " +
            "(:categoryId IS NULL OR e.category.categoryId = :categoryId) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'latest' THEN e.createdDate END DESC, " +
            "CASE WHEN :sortBy = 'highestPrice' THEN e.price END DESC, " +
            "CASE WHEN :sortBy = 'highestHourlyRate' THEN e.price / e.estimatedTime END DESC, " +
            "CASE WHEN :sortBy = 'closestDeadline' THEN e.deadline END ASC")
    Page<Errand> findErrandsByFilters(
            @Param("location") String location,
            @Param("categoryId") Long categoryId,
            @Param("sortBy") String sortBy,
            Pageable pageable
    );
}