package com.dowadream.errand_service.repository;

import com.dowadream.errand_service.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 카테고리 엔티티에 대한 데이터 액세스를 처리하는 리포지토리 인터페이스
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // JpaRepository에서 제공하는 기본 CRUD 메서드들을 상속받아 사용
}