package com.dowadream.errand_service.controller;

import com.dowadream.errand_service.dto.ServiceOfferingDTO;
import com.dowadream.errand_service.service.ServiceOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 서비스 제공 관련 HTTP 요청을 처리하는 컨트롤러 클래스
 */
@RestController
@RequestMapping("/errandservice/service-offerings")
public class ServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    @Autowired
    public ServiceOfferingController(ServiceOfferingService serviceOfferingService) {
        this.serviceOfferingService = serviceOfferingService;
    }

    /**
     * 모든 서비스 제공을 페이징하여 조회합니다.
     *
     * @param pageable 페이징 정보
     * @return 페이징된 서비스 제공 DTO 목록
     */
    @GetMapping
    public ResponseEntity<Page<ServiceOfferingDTO>> getAllServiceOfferings(Pageable pageable) {
        Page<ServiceOfferingDTO> serviceOfferings = serviceOfferingService.getAllServiceOfferings(pageable);
        return ResponseEntity.ok(serviceOfferings);
    }

    /**
     * ID로 특정 서비스 제공을 조회합니다.
     *
     * @param id 서비스 제공 ID
     * @return 서비스 제공 DTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceOfferingDTO> getServiceOfferingById(@PathVariable Long id) {
        ServiceOfferingDTO serviceOffering = serviceOfferingService.getServiceOfferingById(id);
        return ResponseEntity.ok(serviceOffering);
    }

    /**
     * 새로운 서비스 제공을 생성합니다.
     *
     * @param serviceOfferingDTO 서비스 제공 DTO
     * @return 생성된 서비스 제공 DTO
     */
    @PostMapping
    public ResponseEntity<ServiceOfferingDTO> createServiceOffering(@RequestBody ServiceOfferingDTO serviceOfferingDTO) {
        ServiceOfferingDTO createdOffering = serviceOfferingService.createServiceOffering(serviceOfferingDTO);
        return new ResponseEntity<>(createdOffering, HttpStatus.CREATED);
    }

    /**
     * 기존 서비스 제공을 수정합니다.
     *
     * @param id 수정할 서비스 제공 ID
     * @param serviceOfferingDTO 수정할 서비스 제공 정보
     * @return 수정된 서비스 제공 DTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceOfferingDTO> updateServiceOffering(@PathVariable Long id, @RequestBody ServiceOfferingDTO serviceOfferingDTO) {
        ServiceOfferingDTO updatedOffering = serviceOfferingService.updateServiceOffering(id, serviceOfferingDTO);
        return ResponseEntity.ok(updatedOffering);
    }

    /**
     * 서비스 제공을 삭제합니다.
     *
     * @param id 삭제할 서비스 제공 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceOffering(@PathVariable Long id) {
        serviceOfferingService.deleteServiceOffering(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 카테고리별로 서비스 제공을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @param pageable 페이징 정보
     * @return 페이징된 서비스 제공 DTO 목록
     */
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ServiceOfferingDTO>> getServiceOfferingsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        Page<ServiceOfferingDTO> serviceOfferings = serviceOfferingService.getServiceOfferingsByCategory(categoryId, pageable);
        return ResponseEntity.ok(serviceOfferings);
    }

    /**
     * 필터를 적용하여 서비스 제공을 조회합니다.
     *
     * @param location 위치
     * @param categoryId 카테고리 ID
     * @param sortBy 정렬 기준
     * @param pageable 페이징 정보
     * @return 페이징된 서비스 제공 DTO 목록
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<ServiceOfferingDTO>> getFilteredServiceOfferings(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sortBy,
            Pageable pageable) {
        Page<ServiceOfferingDTO> serviceOfferings = serviceOfferingService.getFilteredServiceOfferings(location, categoryId, sortBy, pageable);
        return ResponseEntity.ok(serviceOfferings);
    }
}