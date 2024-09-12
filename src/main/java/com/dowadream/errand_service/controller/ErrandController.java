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
 * 심부름 관련 HTTP 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequestMapping("/ErrandService/errands")
public class ErrandController {

    private final ErrandService errandService;

    /**
     * ErrandController 생성자
     * @param errandService 심부름 서비스 인스턴스
     */
    @Autowired
    public ErrandController(ErrandService errandService) {
        this.errandService = errandService;
    }

    /**
     * 모든 심부름을 페이징하여 조회합니다.
     * @param pageable 페이징 정보
     * @return 페이징된 심부름 DTO 목록
     */
    @GetMapping
    public Page<ErrandDTO> getAllErrands(Pageable pageable) {
        return errandService.getAllErrands(pageable);
    }

    /**
     * 특정 ID의 심부름을 조회합니다.
     * @param id 심부름 ID
     * @return 심부름 DTO와 HTTP 상태
     */
    @GetMapping("/{id}")
    public ResponseEntity<ErrandDTO> getErrandById(@PathVariable Long id) {
        return errandService.getErrandById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 새로운 심부름을 생성합니다.
     * @param errandDTO 심부름 DTO
     * @return 생성된 심부름 DTO와 HTTP 상태
     */
    @PostMapping
    public ResponseEntity<ErrandDTO> createErrand(@RequestBody ErrandDTO errandDTO) {
        ErrandDTO createdErrand = errandService.createErrand(errandDTO);
        return ResponseEntity.ok(createdErrand);
    }

    /**
     * 기존 심부름을 수정합니다.
     * @param id 수정할 심부름 ID
     * @param errandDTO 수정할 심부름 정보
     * @return 수정된 심부름 DTO와 HTTP 상태
     */
    @PutMapping("/{id}")
    public ResponseEntity<ErrandDTO> updateErrand(@PathVariable Long id, @RequestBody ErrandDTO errandDTO) {
        ErrandDTO updatedErrand = errandService.updateErrand(id, errandDTO);
        return ResponseEntity.ok(updatedErrand);
    }

    /**
     * 심부름을 삭제합니다.
     * @param id 삭제할 심부름 ID
     * @return HTTP 상태
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteErrand(@PathVariable Long id) {
        errandService.deleteErrand(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 카테고리별로 심부름을 조회합니다.
     * @param categoryId 카테고리 ID
     * @param pageable 페이징 정보
     * @return 페이징된 심부름 DTO 목록
     */
    @GetMapping("/category/{categoryId}")
    public Page<ErrandDTO> getErrandsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        return errandService.getErrandsByCategory(categoryId, pageable);
    }

    /**
     * 필터를 적용하여 심부름을 조회합니다.
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
     * 심부름을 수락합니다.
     * @param id 심부름 ID
     * @param errandDTO 심부름 DTO
     * @return 수정된 심부름 DTO와 HTTP 상태
     */
    @PutMapping("/{id}/accept")
    public ResponseEntity<ErrandDTO> acceptErrand(@PathVariable Long id, @RequestBody ErrandDTO errandDTO) {
        ErrandDTO updatedErrand = errandService.acceptErrand(id, errandDTO);
        return ResponseEntity.ok(updatedErrand);
    }
}