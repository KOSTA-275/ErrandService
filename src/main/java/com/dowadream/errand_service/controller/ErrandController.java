package com.dowadream.errand_service.controller;

import com.dowadream.errand_service.dto.ErrandDTO;
import com.dowadream.errand_service.entity.Errand;
import com.dowadream.errand_service.service.ErrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 심부름 컨트롤러
 * 심부름 관련 HTTP 요청 처리
 */
@RestController
@RequestMapping("/ErrandService/errands")
public class ErrandController {

    private final ErrandService errandService;

    @Autowired
    public ErrandController(ErrandService errandService) {
        this.errandService = errandService;
    }

    /**
     * 모든 심부름 페이징 조회
     *
     * @param pageable 페이징 정보
     * @return 페이징된 심부름 DTO 목록
     */
    @GetMapping
    public Page<ErrandDTO> getAllErrands(Pageable pageable) {
        return errandService.getAllErrands(pageable);
    }

    /**
     * ID로 심부름 조회
     *
     * @param id 심부름 ID
     * @return 심부름 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ErrandDTO> getErrandById(@PathVariable Long id) {
        return errandService.getErrandById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 새로운 심부름 생성
     *
     * @param errandDTO 심부름 DTO
     * @return 생성된 심부름 DTO
     */
    @PostMapping
    public ResponseEntity<ErrandDTO> createErrand(@RequestBody ErrandDTO errandDTO) {
        ErrandDTO createdErrand = errandService.createErrand(errandDTO);
        return ResponseEntity.ok(createdErrand);
    }

    /**
     * 기존 심부름 수정
     *
     * @param id 심부름 ID
     * @param errandDTO 수정할 심부름 정보
     * @return 수정된 심부름 DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ErrandDTO> updateErrand(@PathVariable Long id, @RequestBody ErrandDTO errandDTO) {
        ErrandDTO updatedErrand = errandService.updateErrand(id, errandDTO);
        return ResponseEntity.ok(updatedErrand);
    }

    /**
     * 심부름 삭제
     *
     * @param id 삭제할 심부름 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteErrand(@PathVariable Long id) {
        errandService.deleteErrand(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리별 심부름 조회
     *
     * @param categoryId 카테고리 ID
     * @param pageable 페이징 정보
     * @return 페이징된 심부름 DTO 목록
     */
    @GetMapping("/category/{categoryId}")
    public Page<ErrandDTO> getErrandsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        return errandService.getErrandsByCategory(categoryId, pageable);
    }

    /**
     * 필터 적용 심부름 조회
     *
     * @param location 위치
     * @param categoryId 카테고리 ID
     * @param sortBy 정렬 기준
     * @param pageable 페이징 정보
     * @return 페이징된 심부름 DTO 목록
     */
    @GetMapping("/filter")
    public Page<ErrandDTO> getFilteredErrands(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sortBy,
            Pageable pageable) {
        return errandService.getFilteredErrands(location, categoryId, sortBy, pageable);
    }

    /**
     * 심부름 상태 업데이트
     *
     * @param id 심부름 ID
     * @param status 새로운 상태
     * @return 업데이트된 심부름 DTO
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ErrandDTO> updateErrandStatus(@PathVariable Long id, @RequestParam Errand.ErrandStatus status) {
        ErrandDTO updatedErrand = errandService.updateErrandStatus(id, status);
        return ResponseEntity.ok(updatedErrand);
    }
}