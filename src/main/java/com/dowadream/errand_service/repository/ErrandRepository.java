package com.dowadream.errand_service.repository;

import com.dowadream.errand_service.entity.Errand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrandRepository extends JpaRepository<Errand, Long> {
    List<Errand> findByCategoryCategoryId(Long categoryId);
}