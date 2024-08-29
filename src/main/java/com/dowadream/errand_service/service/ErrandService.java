package com.dowadream.errand_service.service;

import com.dowadream.errand_service.dto.ErrandDTO;
import com.dowadream.errand_service.entity.Category;
import com.dowadream.errand_service.entity.Errand;
import com.dowadream.errand_service.entity.Image;
import com.dowadream.errand_service.exception.ResourceNotFoundException;
import com.dowadream.errand_service.repository.CategoryRepository;
import com.dowadream.errand_service.repository.ErrandRepository;
import com.dowadream.errand_service.repository.ImageRepository;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class ErrandService {

    private final ErrandRepository errandRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public ErrandService(ErrandRepository errandRepository, CategoryRepository categoryRepository,
                         ImageRepository imageRepository, FileStorageService fileStorageService) {
        this.errandRepository = errandRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.fileStorageService = fileStorageService;
    }

    public Page<ErrandDTO> getAllErrands(Pageable pageable) {
        return errandRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Optional<ErrandDTO> getErrandById(Long id) {
        return errandRepository.findById(id).map(this::convertToDTO);
    }

    public ErrandDTO createErrand(ErrandDTO errandDTO) throws IOException {
        Errand errand = convertToEntity(errandDTO);

        if (errandDTO.getImages() != null && !errandDTO.getImages().isEmpty()) {
            List<Image> images = uploadImages(errandDTO.getImages());
            images.forEach(errand::addImage);
        }

        return convertToDTO(errandRepository.save(errand));
    }

    public ErrandDTO updateErrand(Long id, ErrandDTO errandDTO) throws IOException {
        Errand errand = errandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Errand not found with id: " + id));
        updateErrandFromDTO(errand, errandDTO);

        if (errandDTO.getImages() != null && !errandDTO.getImages().isEmpty()) {
            List<Image> images = uploadImages(errandDTO.getImages());
            images.forEach(errand::addImage);
        }

        return convertToDTO(errandRepository.save(errand));
    }

    public void deleteErrand(Long id) {
        errandRepository.deleteById(id);
    }

    public Page<ErrandDTO> getErrandsByCategory(Long categoryId, Pageable pageable) {
        return errandRepository.findByCategoryCategoryId(categoryId, pageable).map(this::convertToDTO);
    }

    public Page<ErrandDTO> getFilteredErrands(String location, Long categoryId, String sortBy, Pageable pageable) {
        return errandRepository.findErrandsByFilters(location, categoryId, sortBy, pageable).map(this::convertToDTO);
    }

    public ErrandDTO updateErrandStatus(Long id, Errand.ErrandStatus newStatus) {
        Errand errand = errandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Errand not found with id: " + id));
        errand.setStatus(newStatus);
        return convertToDTO(errandRepository.save(errand));
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
            image.setImageType(Image.ImageType.ERRAND_REQUEST);
            images.add(imageRepository.save(image));
        }
        return images;
    }

    private ErrandDTO convertToDTO(Errand errand) {
        ErrandDTO dto = new ErrandDTO();
        dto.setErrandSeq(errand.getErrandSeq());
        dto.setTitle(errand.getTitle());
        dto.setDescription(errand.getDescription());
        dto.setRequesterSeq(errand.getRequesterSeq());
        dto.setRunnerSeq(errand.getRunnerSeq());
        dto.setStatus(errand.getStatus().name());
        dto.setCategoryId(errand.getCategory().getCategoryId());
        dto.setCreatedDate(errand.getCreatedDate());
        dto.setUpdatedDate(errand.getUpdatedDate());
        dto.setLocation(errand.getLocation());
        dto.setPrice(errand.getPrice());
        dto.setEstimatedTime(errand.getEstimatedTime());
        dto.setDeadline(errand.getDeadline());
        return dto;
    }

    private Errand convertToEntity(ErrandDTO dto) {
        Errand errand = new Errand();
        updateErrandFromDTO(errand, dto);
        return errand;
    }

    private void updateErrandFromDTO(Errand errand, ErrandDTO dto) {
        errand.setTitle(dto.getTitle());
        errand.setDescription(dto.getDescription());
        errand.setRequesterSeq(dto.getRequesterSeq());
        errand.setRunnerSeq(dto.getRunnerSeq());
        errand.setStatus(Errand.ErrandStatus.valueOf(dto.getStatus()));
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));
        errand.setCategory(category);
        errand.setLocation(dto.getLocation());
        errand.setPrice(dto.getPrice());
        errand.setEstimatedTime(dto.getEstimatedTime());
        errand.setDeadline(dto.getDeadline());
    }
}