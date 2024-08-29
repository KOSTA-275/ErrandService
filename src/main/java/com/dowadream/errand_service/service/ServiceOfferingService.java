package com.dowadream.errand_service.service;

import com.dowadream.errand_service.dto.ServiceOfferingDTO;
import com.dowadream.errand_service.entity.Category;
import com.dowadream.errand_service.entity.Image;
import com.dowadream.errand_service.entity.ServiceOffering;
import com.dowadream.errand_service.exception.ResourceNotFoundException;
import com.dowadream.errand_service.repository.CategoryRepository;
import com.dowadream.errand_service.repository.ImageRepository;
import com.dowadream.errand_service.repository.ServiceOfferingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServiceOfferingService {

    private final ServiceOfferingRepository serviceOfferingRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public ServiceOfferingService(ServiceOfferingRepository serviceOfferingRepository,
                                  CategoryRepository categoryRepository,
                                  ImageRepository imageRepository,
                                  FileStorageService fileStorageService) {
        this.serviceOfferingRepository = serviceOfferingRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.fileStorageService = fileStorageService;
    }

    public Page<ServiceOfferingDTO> getAllServiceOfferings(Pageable pageable) {
        return serviceOfferingRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Optional<ServiceOfferingDTO> getServiceOfferingById(Long id) {
        return serviceOfferingRepository.findById(id).map(this::convertToDTO);
    }

    public ServiceOfferingDTO createServiceOffering(ServiceOfferingDTO dto) throws IOException {
        ServiceOffering serviceOffering = convertToEntity(dto);

        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            List<Image> images = uploadImages(dto.getImages());
            images.forEach(serviceOffering::addImage);
        }

        return convertToDTO(serviceOfferingRepository.save(serviceOffering));
    }

    public ServiceOfferingDTO updateServiceOffering(Long id, ServiceOfferingDTO dto) throws IOException {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Service offering not found with id: " + id));
        updateServiceOfferingFromDTO(serviceOffering, dto);

        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            List<Image> images = uploadImages(dto.getImages());
            images.forEach(serviceOffering::addImage);
        }

        return convertToDTO(serviceOfferingRepository.save(serviceOffering));
    }

    public void deleteServiceOffering(Long id) {
        serviceOfferingRepository.deleteById(id);
    }

    public Page<ServiceOfferingDTO> getServiceOfferingsByCategory(Long categoryId, Pageable pageable) {
        return serviceOfferingRepository.findByCategoryCategoryId(categoryId, pageable).map(this::convertToDTO);
    }

    public Page<ServiceOfferingDTO> getFilteredServiceOfferings(String location, Long categoryId, String sortBy, Pageable pageable) {
        return serviceOfferingRepository.findServiceOfferingsByFilters(location, categoryId, sortBy, pageable)
                .map(this::convertToDTO);
    }

    private List<Image> uploadImages(List<MultipartFile> files) throws IOException {
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            String fileName = fileStorageService.storeFile(file);
            Image image = new Image();
            image.setFileName(fileName);
            image.setFilePath(fileStorageService.getFileStorageLocation().resolve(fileName).toString());
            image.setFileType(file.getContentType());
            image.setFileSize((int) file.getSize());
            image.setImageType(Image.ImageType.SERVICE_OFFERING);
            images.add(imageRepository.save(image));
        }
        return images;
    }

    private ServiceOfferingDTO convertToDTO(ServiceOffering serviceOffering) {
        ServiceOfferingDTO dto = new ServiceOfferingDTO();
        dto.setId(serviceOffering.getId());
        dto.setTitle(serviceOffering.getTitle());
        dto.setDescription(serviceOffering.getDescription());
        dto.setPriceRange(serviceOffering.getPriceRange());
        dto.setLocation(serviceOffering.getLocation());
        dto.setCategoryId(serviceOffering.getCategory().getCategoryId());
        dto.setProviderId(serviceOffering.getProviderId());
        dto.setCreatedDate(serviceOffering.getCreatedDate());
        dto.setCompletedTasks(serviceOffering.getCompletedTasks());
        // 평균 평점 계산
        if (!serviceOffering.getReviews().isEmpty()) {
            double avgRating = serviceOffering.getReviews().stream()
                    .mapToInt(review -> review.getRating())
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(avgRating);
        }
        return dto;
    }

    private ServiceOffering convertToEntity(ServiceOfferingDTO dto) {
        ServiceOffering serviceOffering = new ServiceOffering();
        updateServiceOfferingFromDTO(serviceOffering, dto);
        return serviceOffering;
    }

    private void updateServiceOfferingFromDTO(ServiceOffering serviceOffering, ServiceOfferingDTO dto) {
        serviceOffering.setTitle(dto.getTitle());
        serviceOffering.setDescription(dto.getDescription());
        serviceOffering.setPriceRange(dto.getPriceRange());
        serviceOffering.setLocation(dto.getLocation());
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));
        serviceOffering.setCategory(category);
        serviceOffering.setProviderId(dto.getProviderId());
    }

    public void incrementCompletedTasks(Long serviceOfferingId) {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(serviceOfferingId)
                .orElseThrow(() -> new ResourceNotFoundException("Service offering not found with id: " + serviceOfferingId));
        serviceOffering.incrementCompletedTasks();
        serviceOfferingRepository.save(serviceOffering);
    }
}