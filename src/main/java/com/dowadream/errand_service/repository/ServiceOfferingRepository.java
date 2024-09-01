package com.dowadream.errand_service.repository;

import com.dowadream.errand_service.entity.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 서비스 제공 엔티티에 대한 데이터 액세스를 처리하는 리포지토리 인터페이스
 */
@Repository
public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> {

    /**
     * 페이징 처리된 모든 서비스 제공 목록을 조회합니다.
     * Oracle 데이터베이스의 ROWNUM을 사용하여 페이징을 구현합니다.
     *
     * @param startRow 시작 행 번호
     * @param endRow 끝 행 번호
     * @return 페이징 처리된 서비스 제공 목록
     */
    @Query(value = "SELECT * FROM " +
            "(SELECT a.*, ROWNUM rnum FROM " +
            "(SELECT * FROM service_offerings ORDER BY id) a " +
            "WHERE ROWNUM <= :endRow) " +
            "WHERE rnum > :startRow",
            nativeQuery = true)
    List<ServiceOffering> findAllWithPagination(@Param("startRow") int startRow, @Param("endRow") int endRow);

    /**
     * 전체 서비스 제공 수를 조회합니다.
     *
     * @return 전체 서비스 제공 수
     */
    @Query(value = "SELECT COUNT(*) FROM service_offerings", nativeQuery = true)
    long countServiceOfferings();

    /**
     * 카테고리별로 서비스 제공을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @param startRow 시작 행 번호
     * @param endRow 끝 행 번호
     * @return 카테고리별 페이징 처리된 서비스 제공 목록
     */
    @Query(value = "SELECT * FROM " +
            "(SELECT a.*, ROWNUM rnum FROM " +
            "(SELECT * FROM service_offerings WHERE category_id = :categoryId ORDER BY id) a " +
            "WHERE ROWNUM <= :endRow) " +
            "WHERE rnum > :startRow",
            nativeQuery = true)
    List<ServiceOffering> findByCategoryWithPagination(@Param("categoryId") Long categoryId,
                                                       @Param("startRow") int startRow,
                                                       @Param("endRow") int endRow);

    /**
     * 카테고리별 전체 서비스 제공 수를 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @return 카테고리별 전체 서비스 제공 수
     */
    @Query(value = "SELECT COUNT(*) FROM service_offerings WHERE category_id = :categoryId", nativeQuery = true)
    long countServiceOfferingsByCategory(@Param("categoryId") Long categoryId);

    /**
     * 필터를 적용하여 서비스 제공을 조회합니다.
     * 위치, 카테고리, 정렬 기준에 따라 서비스 제공을 필터링하고 정렬합니다.
     * Oracle 데이터베이스의 ROWNUM을 사용하여 페이징을 구현합니다.
     *
     * @param location 위치 (null이면 모든 위치)
     * @param categoryId 카테고리 ID (null이면 모든 카테고리)
     * @param sortBy 정렬 기준 ('latest', 'highestRating', 'mostTasks' 중 하나)
     * @param startRow 시작 행 번호
     * @param endRow 끝 행 번호
     * @return 필터링 및 정렬된 서비스 제공 목록
     */
    @Query(value = "SELECT * FROM " +
            "(SELECT a.*, ROWNUM rnum FROM " +
            "(SELECT so.* FROM service_offerings so " +
            "WHERE (:location IS NULL OR so.location = :location) " +
            "AND (:categoryId IS NULL OR so.category_id = :categoryId) " +
            "ORDER BY " +
            "CASE WHEN :sortBy = 'latest' THEN so.created_date END DESC, " +
            "CASE WHEN :sortBy = 'highestRating' THEN (SELECT AVG(r.rating) FROM reviews r WHERE r.service_offering_id = so.id) END DESC, " +
            "CASE WHEN :sortBy = 'mostTasks' THEN so.completed_tasks END DESC) a " +
            "WHERE ROWNUM <= :endRow) " +
            "WHERE rnum > :startRow",
            nativeQuery = true)
    List<ServiceOffering> findServiceOfferingsByFilters(
            @Param("location") String location,
            @Param("categoryId") Long categoryId,
            @Param("sortBy") String sortBy,
            @Param("startRow") int startRow,
            @Param("endRow") int endRow);

    /**
     * 필터를 적용한 서비스 제공의 총 개수를 조회합니다.
     * 위치와 카테고리에 따라 필터링된 서비스 제공의 총 개수를 반환합니다.
     *
     * @param location 위치 (null이면 모든 위치)
     * @param categoryId 카테고리 ID (null이면 모든 카테고리)
     * @return 필터링된 서비스 제공의 총 개수
     */
    @Query(value = "SELECT COUNT(*) FROM service_offerings so " +
            "WHERE (:location IS NULL OR so.location = :location) " +
            "AND (:categoryId IS NULL OR so.category_id = :categoryId)",
            nativeQuery = true)
    long countServiceOfferingsByFilters(
            @Param("location") String location,
            @Param("categoryId") Long categoryId);
}