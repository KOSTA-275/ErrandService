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
}