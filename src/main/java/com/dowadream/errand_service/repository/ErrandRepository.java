package com.dowadream.errand_service.repository;

import com.dowadream.errand_service.entity.Errand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrandRepository extends JpaRepository<Errand, Long> {
    @EntityGraph(attributePaths = {"category"})
    Page<Errand> findAll(Pageable pageable);

    Page<Errand> findByCategoryCategoryId(Long categoryId, Pageable pageable);
}