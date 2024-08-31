package com.dowadream.errand_service.repository;

import com.dowadream.errand_service.entity.Errand;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrandRepository extends JpaRepository<Errand, Long> {
    @Query(value = "SELECT * FROM " +
            "(SELECT a.*, ROWNUM rnum FROM " +
            "(SELECT e.* FROM errands e ORDER BY e.errand_seq) a " +
            "WHERE ROWNUM <= :endRow) " +
            "WHERE rnum > :startRow",
            nativeQuery = true)
    List<Errand> findAllWithPagination(@Param("startRow") int startRow, @Param("endRow") int endRow);

    @Query(value = "SELECT COUNT(*) FROM errands", nativeQuery = true)
    long countErrands();

    @Query(value = "SELECT * FROM " +
            "(SELECT a.*, ROWNUM rnum FROM " +
            "(SELECT e.* FROM errands e WHERE e.category_id = :categoryId ORDER BY e.errand_seq) a " +
            "WHERE ROWNUM <= :endRow) " +
            "WHERE rnum > :startRow",
            nativeQuery = true)
    List<Errand> findByCategoryWithPagination(@Param("categoryId") Long categoryId, @Param("startRow") int startRow, @Param("endRow") int endRow);

    @Query(value = "SELECT COUNT(*) FROM errands WHERE category_id = :categoryId", nativeQuery = true)
    long countErrandsByCategory(@Param("categoryId") Long categoryId);

    @Query(value = "SELECT * FROM " +
            "(SELECT a.*, ROWNUM rnum FROM " +
            "(SELECT e.* FROM errands e " +
            "WHERE (:location IS NULL OR e.location = :location) " +
            "AND (:categoryId IS NULL OR e.category_id = :categoryId) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'latest' THEN e.created_date END DESC, " +
            "CASE WHEN :sortBy = 'highestPrice' THEN e.price END DESC, " +
            "CASE WHEN :sortBy = 'highestHourlyRate' THEN e.price / e.estimated_time END DESC, " +
            "CASE WHEN :sortBy = 'closestDeadline' THEN e.deadline END ASC) a " +
            "WHERE ROWNUM <= :endRow) " +
            "WHERE rnum > :startRow",
            nativeQuery = true)
    List<Errand> findErrandsByFilters(@Param("location") String location,
                                      @Param("categoryId") Long categoryId,
                                      @Param("sortBy") String sortBy,
                                      @Param("startRow") int startRow,
                                      @Param("endRow") int endRow);

    @Query(value = "SELECT COUNT(*) FROM errands e " +
            "WHERE (:location IS NULL OR e.location = :location) " +
            "AND (:categoryId IS NULL OR e.category_id = :categoryId)",
            nativeQuery = true)
    long countErrandsByFilters(@Param("location") String location, @Param("categoryId") Long categoryId);
}