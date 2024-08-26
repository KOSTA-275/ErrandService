package com.dowadream.errand_service.service;

import com.dowadream.errand_service.entity.ServiceOffering;
import com.dowadream.errand_service.repository.ServiceOfferingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 서비스 제공 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
 */
@Service
public class ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;

    @Autowired
    public ServiceOfferingService(ServiceOfferingRepository serviceOfferingRepository) {
        this.serviceOfferingRepository = serviceOfferingRepository;
    }

    /**
     * 모든 서비스 제공 정보를 조회합니다.
     * @return 전체 서비스 제공 목록
     */
    public List<ServiceOffering> getAllServiceOfferings() {
        return serviceOfferingRepository.findAll();
    }

    /**
     * 특정 ID의 서비스 제공 정보를 조회합니다.
     * @param id 서비스 제공 ID
     * @return 조회된 서비스 제공 정보 (Optional)
     */
    public Optional<ServiceOffering> getServiceOfferingById(Long id) {
        return serviceOfferingRepository.findById(id);
    }

    /**
     * 새로운 서비스 제공 정보를 생성합니다.
     * @param serviceOffering 생성할 서비스 제공 정보
     * @return 생성된 서비스 제공 정보
     */
    public ServiceOffering createServiceOffering(ServiceOffering serviceOffering) {
        return serviceOfferingRepository.save(serviceOffering);
    }

    /**
     * 기존 서비스 제공 정보를 수정합니다.
     * @param id 수정할 서비스 제공 ID
     * @param serviceOfferingDetails 수정할 서비스 제공 정보
     * @return 수정된 서비스 제공 정보
     */
    public ServiceOffering updateServiceOffering(Long id, ServiceOffering serviceOfferingDetails) {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("서비스 제공 정보를 찾을 수 없습니다"));

        serviceOffering.setTitle(serviceOfferingDetails.getTitle());
        serviceOffering.setDescription(serviceOfferingDetails.getDescription());
        serviceOffering.setPriceRange(serviceOfferingDetails.getPriceRange());
        serviceOffering.setLocation(serviceOfferingDetails.getLocation());
        serviceOffering.setCategory(serviceOfferingDetails.getCategory());
        serviceOffering.setProviderId(serviceOfferingDetails.getProviderId());

        return serviceOfferingRepository.save(serviceOffering);
    }

    /**
     * 특정 서비스 제공 정보를 삭제합니다.
     * @param id 삭제할 서비스 제공 ID
     */
    public void deleteServiceOffering(Long id) {
        serviceOfferingRepository.deleteById(id);
    }

    /**
     * 특정 카테고리에 속한 서비스 제공 정보를 조회합니다.
     * @param categoryId 카테고리 ID
     * @return 해당 카테고리의 서비스 제공 목록
     */
    public List<ServiceOffering> getServiceOfferingsByCategory(Long categoryId) {
        return serviceOfferingRepository.findAll().stream()
                .filter(offering -> offering.getCategory().getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }
}