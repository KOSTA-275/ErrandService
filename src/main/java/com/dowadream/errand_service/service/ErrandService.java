package com.dowadream.errand_service.service;

import com.dowadream.errand_service.dto.ErrandDTO;
import com.dowadream.errand_service.entity.Category;
import com.dowadream.errand_service.entity.Errand;
import com.dowadream.errand_service.entity.Image;
import com.dowadream.errand_service.repository.CategoryRepository;
import com.dowadream.errand_service.repository.ErrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ErrandService {

    private final ErrandRepository errandRepository;
    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    @Autowired
    public ErrandService(ErrandRepository errandRepository, CategoryRepository categoryRepository, FileStorageService fileStorageService) {
        this.errandRepository = errandRepository;
        this.categoryRepository = categoryRepository;
        this.fileStorageService = fileStorageService;
    }

    public List<Errand> getAllErrands() {
        return errandRepository.findAll();
    }

    public Optional<Errand> getErrandById(Long id) {
        return errandRepository.findById(id);
    }

    @Transactional
    public Errand createErrand(ErrandDTO errandDTO) throws IOException {
        Errand errand = new Errand();
        errand.setTitle(errandDTO.getTitle());
        errand.setDescription(errandDTO.getDescription());
        errand.setStatus(errandDTO.getStatus());
        errand.setRequesterSeq(errandDTO.getRequesterSeq());
        errand.setRunnerSeq(errandDTO.getRunnerSeq());

        Category category = categoryRepository.findById(errandDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        errand.setCategory(category);

        errand = errandRepository.save(errand);

        if (errandDTO.getImages() != null && !errandDTO.getImages().isEmpty()) {
            for (MultipartFile file : errandDTO.getImages()) {
                String fileName = fileStorageService.storeFile(file);
                Image image = new Image();
                image.setFileName(fileName);
                image.setFilePath(fileStorageService.getFileStorageLocation().resolve(fileName).toString());
                image.setFileType(file.getContentType());
                image.setFileSize((int) file.getSize());
                image.setErrand(errand);
                image.setRelatedType(Image.RelatedType.ERRAND);
                errand.getImages().add(image);
            }
        }

        return errand;
    }

    @Transactional
    public Errand updateErrand(Long id, ErrandDTO errandDTO) throws IOException {
        Errand errand = errandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("심부름을 찾을 수 없습니다"));

        errand.setTitle(errandDTO.getTitle());
        errand.setDescription(errandDTO.getDescription());
        errand.setStatus(errandDTO.getStatus());
        errand.setRequesterSeq(errandDTO.getRequesterSeq());
        errand.setRunnerSeq(errandDTO.getRunnerSeq());

        Category category = categoryRepository.findById(errandDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        errand.setCategory(category);

        if (errandDTO.getImages() != null && !errandDTO.getImages().isEmpty()) {
            for (MultipartFile file : errandDTO.getImages()) {
                String fileName = fileStorageService.storeFile(file);
                Image image = new Image();
                image.setFileName(fileName);
                image.setFilePath(fileStorageService.getFileStorageLocation().resolve(fileName).toString());
                image.setFileType(file.getContentType());
                image.setFileSize((int) file.getSize());
                errand.addImage(image);
            }
        }

        return errandRepository.save(errand);
    }

    public void deleteErrand(Long id) {
        errandRepository.deleteById(id);
    }

    public List<Errand> getErrandsByCategory(Long categoryId) {
        return errandRepository.findAll().stream()
                .filter(errand -> errand.getCategory().getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }
}