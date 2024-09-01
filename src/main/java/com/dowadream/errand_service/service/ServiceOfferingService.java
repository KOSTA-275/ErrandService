package com.dowadream.errand_service.service;

import com.dowadream.errand_service.dto.ServiceOfferingDTO;
import com.dowadream.errand_service.entity.Category;
import com.dowadream.errand_service.entity.Image;
import com.dowadream.errand_service.entity.ServiceOffering;
import com.dowadream.errand_service.entity.Review;
import com.dowadream.errand_service.exception.BadRequestException;
import com.dowadream.errand_service.exception.ResourceNotFoundException;
import com.dowadream.errand_service.repository.CategoryRepository;
import com.dowadream.errand_service.repository.ServiceOfferingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 서비스 제공 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@Transactional
public class ServiceOfferingService {
    private final ServiceOfferingRepository serviceOfferingRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public ServiceOfferingService(ServiceOfferingRepository serviceOfferingRepository,
                                  CategoryRepository categoryRepository,
                                  FileStorageService fileStorageService) {
        this.serviceOfferingRepository = serviceOfferingRepository;
        this.categoryRepository = categoryRepository;
        this.fileStorageService = fileStorageService;
    }

    /**
     * 페이징 처리된 모든 서비스 제공 목록을 조회합니다.
     *
     * @param pageable 페이징 정보
     * @return 페이징 처리된 서비스 제공 DTO 목록
     */
    public Page<ServiceOfferingDTO> getAllServiceOfferings(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startRow = currentPage * pageSize;
        int endRow = (currentPage + 1) * pageSize;

        List<ServiceOffering> serviceOfferings = serviceOfferingRepository.findAllWithPagination(startRow, endRow);
        long total = serviceOfferingRepository.countServiceOfferings();

        List<ServiceOfferingDTO> dtoList = serviceOfferings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    /**
     * 새로운 서비스 제공을 생성합니다.
     *
     * @param dto 서비스 제공 DTO
     * @return 생성된 서비스 제공 DTO
     */
    public ServiceOfferingDTO createServiceOffering(ServiceOfferingDTO dto) {
        try {
            ServiceOffering serviceOffering = new ServiceOffering();
            updateServiceOfferingFromDTO(serviceOffering, dto);

            if (dto.getImages() != null && !dto.getImages().isEmpty()) {
                List<Image> images = uploadImages(dto.getImages());
                images.forEach(serviceOffering::addImage);
            }

            ServiceOffering savedOffering = serviceOfferingRepository.save(serviceOffering);
            return convertToDTO(savedOffering);
        } catch (IOException e) {
            throw new BadRequestException("이미지 처리 중 오류 발생: " + e.getMessage());
        }
    }

    /**
     * 서비스 제공을 업데이트합니다.
     *
     * @param id 서비스 제공 ID
     * @param dto 업데이트할 서비스 제공 정보
     * @return 업데이트된 서비스 제공 DTO
     */
    public ServiceOfferingDTO updateServiceOffering(Long id, ServiceOfferingDTO dto) {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("서비스 제공을 찾을 수 없습니다. ID: " + id));

        updateServiceOfferingFromDTO(serviceOffering, dto);

        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            try {
                List<Image> newImages = uploadImages(dto.getImages());
                serviceOffering.getImages().clear();
                newImages.forEach(serviceOffering::addImage);
            } catch (IOException e) {
                throw new BadRequestException("이미지 처리 중 오류 발생: " + e.getMessage());
            }
        }

        ServiceOffering updatedOffering = serviceOfferingRepository.save(serviceOffering);
        return convertToDTO(updatedOffering);
    }

    /**
     * 서비스 제공을 삭제합니다.
     *
     * @param id 삭제할 서비스 제공 ID
     */
    public void deleteServiceOffering(Long id) {
        if (!serviceOfferingRepository.existsById(id)) {
            throw new ResourceNotFoundException("서비스 제공을 찾을 수 없습니다. ID: " + id);
        }
        serviceOfferingRepository.deleteById(id);
    }

    /**
     * 카테고리별로 서비스 제공을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @param pageable 페이징 정보
     * @return 카테고리별 페이징 처리된 서비스 제공 DTO 목록
     */
    public Page<ServiceOfferingDTO> getServiceOfferingsByCategory(Long categoryId, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startRow = currentPage * pageSize;
        int endRow = (currentPage + 1) * pageSize;

        List<ServiceOffering> serviceOfferings = serviceOfferingRepository.findByCategoryWithPagination(categoryId, startRow, endRow);
        long total = serviceOfferingRepository.countServiceOfferingsByCategory(categoryId);

        List<ServiceOfferingDTO> dtoList = serviceOfferings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    /**
     * DTO의 정보로 서비스 제공 엔티티를 업데이트합니다.
     *
     * @param serviceOffering 업데이트할 서비스 제공 엔티티
     * @param dto 서비스 제공 DTO
     */
    private void updateServiceOfferingFromDTO(ServiceOffering serviceOffering, ServiceOfferingDTO dto) {
        serviceOffering.setTitle(dto.getTitle());
        serviceOffering.setDescription(dto.getDescription());
        serviceOffering.setPriceRange(dto.getPriceRange());
        serviceOffering.setLocation(dto.getLocation());
        serviceOffering.setProviderId(dto.getProviderId());

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("카테고리를 찾을 수 없습니다. ID: " + dto.getCategoryId()));
            serviceOffering.setCategory(category);
        }
    }

    /**
     * 서비스 제공 엔티티를 DTO로 변환합니다.
     *
     * @param serviceOffering 서비스 제공 엔티티
     * @return 서비스 제공 DTO
     */
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

        // 이미지 경로 설정
        List<String> imagePaths = serviceOffering.getImages().stream()
                .map(Image::getFilePath)
                .collect(Collectors.toList());
        dto.setImagePaths(imagePaths);

        // 평균 평점 계산
        if (!serviceOffering.getReviews().isEmpty()) {
            double avgRating = serviceOffering.getReviews().stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
            dto.setAverageRating(avgRating);
        }

        return dto;
    }

    /**
     * 이미지를 업로드하고 이미지 엔티티 목록을 반환합니다.
     *
     * @param images 업로드할 이미지 파일 목록
     * @return 업로드된 이미지 엔티티 목록
     * @throws IOException 이미지 업로드 중 오류 발생 시
     */
    private List<Image> uploadImages(List<MultipartFile> images) throws IOException {
        List<Image> uploadedImages = new ArrayList<>();
        for (MultipartFile file : images) {
            String fileName = fileStorageService.storeFile(file);
            Image image = new Image();
            image.setFileName(fileName);
            image.setFilePath(fileStorageService.getFileStorageLocation().resolve(fileName).toString());
            image.setFileType(file.getContentType());
            image.setFileSize((int) file.getSize());
            image.setImageType(Image.ImageType.SERVICE_OFFERING);
            uploadedImages.add(image);
        }
        return uploadedImages;
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
    public Page<ServiceOfferingDTO> getFilteredServiceOfferings(String location, Long categoryId, String sortBy, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startRow = currentPage * pageSize;
        int endRow = (currentPage + 1) * pageSize;

        List<ServiceOffering> serviceOfferings = serviceOfferingRepository.findServiceOfferingsByFilters(
                location, categoryId, sortBy, startRow, endRow);
        long total = serviceOfferingRepository.countServiceOfferingsByFilters(location, categoryId);

        List<ServiceOfferingDTO> dtoList = serviceOfferings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtoList, pageable, total);
    }

    /**
     * ID로 서비스 제공을 조회합니다.
     *
     * @param id 서비스 제공 ID
     * @return 서비스 제공 DTO
     * @throws ResourceNotFoundException 해당 ID의 서비스 제공이 없을 경우
     */
    public ServiceOfferingDTO getServiceOfferingById(Long id) {
        ServiceOffering serviceOffering = serviceOfferingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("서비스 제공을 찾을 수 없습니다. ID: " + id));
        return convertToDTO(serviceOffering);
    }
}