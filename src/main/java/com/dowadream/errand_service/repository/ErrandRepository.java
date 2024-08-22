package com.dowadream.errand_service.repository;

import com.dowadream.errand_service.entity.Errand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrandRepository extends JpaRepository<Errand, Long> {
    //추가적인 쿼리 메서드 정의.
}