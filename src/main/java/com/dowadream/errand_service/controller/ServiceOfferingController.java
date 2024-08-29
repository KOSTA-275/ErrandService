package com.dowadream.errand_service.controller;

import com.dowadream.errand_service.dto.ServiceOfferingDTO;
import com.dowadream.errand_service.service.ServiceOfferingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/errandservice/service-offerings")
public class ServiceOfferingController {

    private final ServiceOfferingService serviceOfferingService;

    @Autowired
    public ServiceOfferingController(ServiceOfferingService serviceOfferingService) {
        this.serviceOfferingService = serviceOfferingService;
    }

    @GetMapping
    public Page<ServiceOfferingDTO> getAllServiceOfferings(Pageable pageable) {
        return serviceOfferingService.getAllServiceOfferings(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOfferingDTO> getServiceOfferingById(@PathVariable Long id) {
        return serviceOfferingService.getServiceOfferingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceOfferingDTO> createServiceOffering(@ModelAttribute ServiceOfferingDTO serviceOfferingDTO) {
        try {
            ServiceOfferingDTO createdOffering = serviceOfferingService.createServiceOffering(serviceOfferingDTO);
            return ResponseEntity.ok(createdOffering);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOfferingDTO> updateServiceOffering(@PathVariable Long id, @ModelAttribute ServiceOfferingDTO serviceOfferingDTO) {
        try {
            ServiceOfferingDTO updatedOffering = serviceOfferingService.updateServiceOffering(id, serviceOfferingDTO);
            return ResponseEntity.ok(updatedOffering);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceOffering(@PathVariable Long id) {
        serviceOfferingService.deleteServiceOffering(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/category/{categoryId}")
    public Page<ServiceOfferingDTO> getServiceOfferingsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        return serviceOfferingService.getServiceOfferingsByCategory(categoryId, pageable);
    }

    @GetMapping("/filter")
    public Page<ServiceOfferingDTO> getFilteredServiceOfferings(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String sortBy,
            Pageable pageable) {
        return serviceOfferingService.getFilteredServiceOfferings(location, categoryId, sortBy, pageable);
    }
}