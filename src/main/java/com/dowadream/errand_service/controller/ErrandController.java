package com.dowadream.errand_service.controller;

import com.dowadream.errand_service.entity.Errand;
import com.dowadream.errand_service.service.ErrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 심부름 관련 API 엔드포인트를 제공하는 컨트롤러 클래스입니다.
 */
@RestController
@RequestMapping("/api/errands")
public class ErrandController {

    private final ErrandService errandService;

    @Autowired
    public ErrandController(ErrandService errandService) {
        this.errandService = errandService;
    }

    /**
     * 모든 심부름 정보를 조회합니다.
     * @return 전체 심부름 목록
     */
    @GetMapping
    public List<Errand> getAllErrands() {
        return errandService.getAllErrands();
    }

    /**
     * 특정 ID의 심부름 정보를 조회합니다.
     * @param id 심부름 ID
     * @return 조회된 심부름 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<Errand> getErrandById(@PathVariable Long id) {
        return errandService.getErrandById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 새로운 심부름을 생성합니다.
     * @param errand 생성할 심부름 정보
     * @return 생성된 심부름
     */
    @PostMapping
    public Errand createErrand(@RequestBody Errand errand) {
        return errandService.createErrand(errand);
    }

    /**
     * 기존 심부름 정보를 수정합니다.
     * @param id 수정할 심부름 ID
     * @param errandDetails 수정할 심부름 정보
     * @return 수정된 심부름
     */
    @PutMapping("/{id}")
    public ResponseEntity<Errand> updateErrand(@PathVariable Long id, @RequestBody Errand errandDetails) {
        Errand updatedErrand = errandService.updateErrand(id, errandDetails);
        return ResponseEntity.ok(updatedErrand);
    }

    /**
     * 특정 심부름을 삭제합니다.
     * @param id 삭제할 심부름 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteErrand(@PathVariable Long id) {
        errandService.deleteErrand(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 카테고리에 속한 심부름을 조회합니다.
     * @param categoryId 카테고리 ID
     * @return 해당 카테고리의 심부름 목록
     */
    @GetMapping("/category/{categoryId}")
    public List<Errand> getErrandsByCategory(@PathVariable Long categoryId) {
        return errandService.getErrandsByCategory(categoryId);
    }
}