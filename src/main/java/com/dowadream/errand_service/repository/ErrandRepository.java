package com.dowadream.errand_service.repository;

import com.dowadream.errand_service.entity.Errand;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 심부름 엔티티에 대한 데이터 액세스를 처리하는 리포지토리 인터페이스
 */
@Repository
public interface ErrandRepository extends JpaRepository<Errand, Long> {

    /**
     * 페이징 처리된 모든 심부름을 조회합니다.
     * @param startRow 시작 행
     * @param endRow 끝 행
     * @return 페이징된 심부름 목록
     */
    @Query(value = "SELECT * FROM " +
            "(SELECT a.*, ROWNUM rnum FROM " +
            "(SELECT e.* FROM errands e ORDER BY e.errand_seq) a " +
            "WHERE ROWNUM <= :endRow) " +
            "WHERE rnum > :startRow",
            nativeQuery = true)
    List<Errand> findAllWithPagination(@Param("startRow") int startRow, @Param("endRow") int endRow);

    /**
     * 전체 심부름 수를 조회합니다.
     * @return 전체 심부름 수
     */
    @Query(value = "SELECT COUNT(*) FROM errands", nativeQuery = true)
    long countErrands();

    /**
     * 카테고리별로 페이징 처리된 심부름을 조회합니다.
     * @param categoryId 카테고리 ID
     * @param startRow 시작 행
     * @param endRow 끝 행
     * @return 카테고리별 페이징된 심부름 목록
     */
    @Query(value = "SELECT * FROM " +
            "(SELECT a.*, ROWNUM rnum FROM " +
            "(SELECT e.* FROM errands e WHERE e.category_id = :categoryId ORDER BY e.errand_seq) a " +
            "WHERE ROWNUM <= :endRow) " +
            "WHERE rnum > :startRow",
            nativeQuery = true)
    List<Errand> findByCategoryWithPagination(@Param("categoryId") Long categoryId, @Param("startRow") int startRow, @Param("endRow") int endRow);

    /**
     * 카테고리별 전체 심부름 수를 조회합니다.
     * @param categoryId 카테고리 ID
     * @return 카테고리별 전체 심부름 수
     */
    @Query(value = "SELECT COUNT(*) FROM errands WHERE category_id = :categoryId", nativeQuery = true)
    long countErrandsByCategory(@Param("categoryId") Long categoryId);

    /**
     * 필터를 적용하여 심부름을 조회합니다.
     * @param location 위치
     * @param categoryId 카테고리 ID
     * @param sortBy 정렬 기준
     * @param startRow 시작 행
     * @param endRow 끝 행
     * @return 필터링된 심부름 목록
     */
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

    /**
     * 필터를 적용한 전체 심부름 수를 조회합니다.
     * @param location 위치
     * @param categoryId 카테고리 ID
     * @return 필터링된 전체 심부름 수
     */
    @Query(value = "SELECT COUNT(*) FROM errands e " +
            "WHERE (:location IS NULL OR e.location = :location) " +
            "AND (:categoryId IS NULL OR e.category_id = :categoryId)",
            nativeQuery = true)
    long countErrandsByFilters(@Param("location") String location, @Param("categoryId") Long categoryId);
}