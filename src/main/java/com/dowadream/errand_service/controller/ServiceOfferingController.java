package com.dowadream.errand_service.controller;

import com.dowadream.errand_service.entity.ServiceOffering;
import com.dowadream.errand_service.service.ServiceOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 서비스 제공 관련 API 엔드포인트를 제공하는 컨트롤러 클래스입니다.
 */
@RestController
@RequestMapping("/api/service-offerings")
public class ServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    @Autowired
    public ServiceOfferingController(ServiceOfferingService serviceOfferingService) {
        this.serviceOfferingService = serviceOfferingService;
    }

    /**
     * 모든 서비스 제공 정보를 조회합니다.
     * @return 전체 서비스 제공 목록
     */
    @GetMapping
    public List<ServiceOffering> getAllServiceOfferings() {
        return serviceOfferingService.getAllServiceOfferings();
    }

    /**
     * 특정 ID의 서비스 제공 정보를 조회합니다.
     * @param id 서비스 제공 ID
     * @return 조회된 서비스 제공 정보
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceOffering> getServiceOfferingById(@PathVariable Long id) {
        return serviceOfferingService.getServiceOfferingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 새로운 서비스 제공 정보를 생성합니다.
     * @param serviceOffering 생성할 서비스 제공 정보
     * @return 생성된 서비스 제공 정보
     */
    @PostMapping
    public ServiceOffering createServiceOffering(@RequestBody ServiceOffering serviceOffering) {
        return serviceOfferingService.createServiceOffering(serviceOffering);
    }

    /**
     * 기존 서비스 제공 정보를 수정합니다.
     * @param id 수정할 서비스 제공 ID
     * @param serviceOfferingDetails 수정할 서비스 제공 정보
     * @return 수정된 서비스 제공 정보
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceOffering> updateServiceOffering(@PathVariable Long id, @RequestBody ServiceOffering serviceOfferingDetails) {
        ServiceOffering updatedServiceOffering = serviceOfferingService.updateServiceOffering(id, serviceOfferingDetails);
        return ResponseEntity.ok(updatedServiceOffering);
    }

    /**
     * 특정 서비스 제공 정보를 삭제합니다.
     * @param id 삭제할 서비스 제공 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceOffering(@PathVariable Long id) {
        serviceOfferingService.deleteServiceOffering(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 특정 카테고리에 속한 서비스 제공 정보를 조회합니다.
     * @param categoryId 카테고리 ID
     * @return 해당 카테고리의 서비스 제공 목록
     */
    @GetMapping("/category/{categoryId}")
    public List<ServiceOffering> getServiceOfferingsByCategory(@PathVariable Long categoryId) {
        return serviceOfferingService.getServiceOfferingsByCategory(categoryId);
    }
}