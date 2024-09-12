package com.dowadream.errand_service.service;

import com.dowadream.errand_service.dto.ErrandDTO;
import com.dowadream.errand_service.entity.Category;
import com.dowadream.errand_service.entity.Errand;
import com.dowadream.errand_service.exception.ResourceNotFoundException;
import com.dowadream.errand_service.exception.BadRequestException;
import com.dowadream.errand_service.repository.CategoryRepository;
import com.dowadream.errand_service.repository.ErrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 심부름 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@Transactional
public class ErrandService {

    private final ErrandRepository errandRepository;
    private final CategoryRepository categoryRepository;

    /**
     * ErrandService 생성자
     * @param errandRepository 심부름 리포지토리
     * @param categoryRepository 카테고리 리포지토리
     */
    @Autowired
    public ErrandService(ErrandRepository errandRepository, CategoryRepository categoryRepository) {
        this.errandRepository = errandRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * 모든 심부름을 페이징하여 조회합니다.
     * @param pageable 페이징 정보
     * @return 페이징된 심부름 DTO 목록
     */
    public Page<ErrandDTO> getAllErrands(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startRow = currentPage * pageSize;
        int endRow = (currentPage + 1) * pageSize;

        List<Errand> errands = errandRepository.findAllWithPagination(startRow, endRow);
        long total = errandRepository.countErrands();

        List<ErrandDTO> errandDTOs = errands.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(errandDTOs, pageable, total);
    }

    /**
     * ID로 심부름을 조회합니다.
     * @param id 심부름 ID
     * @return 심부름 DTO (Optional)
     */
    public Optional<ErrandDTO> getErrandById(Long id) {
        return errandRepository.findById(id).map(this::convertToDTO);
    }

    /**
     * 카테고리별로 심부름을 페이징하여 조회합니다.
     * @param categoryId 카테고리 ID
     * @param pageable 페이징 정보
     * @return 페이징된 심부름 DTO 목록
     */
    public Page<ErrandDTO> getErrandsByCategory(Long categoryId, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startRow = currentPage * pageSize;
        int endRow = (currentPage + 1) * pageSize;

        List<Errand> errands = errandRepository.findByCategoryWithPagination(categoryId, startRow, endRow);
        long total = errandRepository.countErrandsByCategory(categoryId);

        List<ErrandDTO> errandDTOs = errands.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(errandDTOs, pageable, total);
    }

    /**
     * 필터를 적용하여 심부름을 페이징하여 조회합니다.
     * @param location 위치
     * @param categoryId 카테고리 ID
     * @param sortBy 정렬 기준
     * @param pageable 페이징 정보
     * @return 페이징된 심부름 DTO 목록
     */
    public Page<ErrandDTO> getFilteredErrands(String location, Long categoryId, String sortBy, Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startRow = currentPage * pageSize;
        int endRow = (currentPage + 1) * pageSize;

        List<Errand> errands = errandRepository.findErrandsByFilters(location, categoryId, sortBy, startRow, endRow);
        long total = errandRepository.countErrandsByFilters(location, categoryId);

        List<ErrandDTO> errandDTOs = errands.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(errandDTOs, pageable, total);
    }

    /**
     * 새로운 심부름을 생성합니다.
     * @param errandDTO 심부름 DTO
     * @return 생성된 심부름 DTO
     */
    public ErrandDTO createErrand(ErrandDTO errandDTO) {
        Errand errand = convertToEntity(errandDTO);
        return convertToDTO(errandRepository.save(errand));
    }

    /**
     * 기존 심부름을 수정합니다.
     * @param id 수정할 심부름 ID
     * @param errandDTO 수정할 심부름 정보
     * @return 수정된 심부름 DTO
     */
    public ErrandDTO updateErrand(Long id, ErrandDTO errandDTO) {
        Errand errand = errandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Errand not found with id: " + id));
        updateErrandFromDTO(errand, errandDTO);
        return convertToDTO(errandRepository.save(errand));
    }

    /**
     * 심부름을 삭제합니다.
     * @param id 삭제할 심부름 ID
     */
    public void deleteErrand(Long id) {
        errandRepository.deleteById(id);
    }

    /**
     * 심부름을 수락합니다.
     * @param id 심부름 ID
     * @param errandDTO 심부름 DTO
     * @return 수정된 심부름 DTO
     */
    public ErrandDTO acceptErrand(Long id, ErrandDTO errandDTO) {
        Errand errand = errandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Errand not found with id: " + id));

        if (errand.getStatus() != Errand.ErrandStatus.REQUESTED) {
            throw new BadRequestException("This errand is not available for acceptance.");
        }

        errand.setStatus(Errand.ErrandStatus.IN_PROGRESS);
        errand.setRunnerSeq(errandDTO.getRunnerSeq());
        errand.setRunnerNickname(errandDTO.getRunnerNickname());

        return convertToDTO(errandRepository.save(errand));
    }

    /**
     * 심부름 엔티티를 DTO로 변환합니다.
     * @param errand 심부름 엔티티
     * @return 심부름 DTO
     */
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
        dto.setRequesterNickname(errand.getRequesterNickname());
        dto.setRunnerNickname(errand.getRunnerNickname());
        return dto;
    }

    /**
     * 심부름 DTO를 엔티티로 변환합니다.
     * @param dto 심부름 DTO
     * @return 심부름 엔티티
     */
    private Errand convertToEntity(ErrandDTO dto) {
        Errand errand = new Errand();
        updateErrandFromDTO(errand, dto);
        return errand;
    }

    /**
     * DTO의 정보로 심부름 엔티티를 업데이트합니다.
     * @param errand 업데이트할 심부름 엔티티
     * @param dto 심부름 DTO
     */
    private void updateErrandFromDTO(Errand errand, ErrandDTO dto) {
        errand.setTitle(dto.getTitle());
        errand.setDescription(dto.getDescription());
        errand.setRequesterSeq(dto.getRequesterSeq());
        errand.setRunnerSeq(dto.getRunnerSeq());
        errand.setRequesterNickname(dto.getRequesterNickname());
        errand.setRunnerNickname(dto.getRunnerNickname());

        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            try {
                errand.setStatus(Errand.ErrandStatus.valueOf(dto.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                errand.setStatus(Errand.ErrandStatus.REQUESTED);
            }
        } else {
            errand.setStatus(Errand.ErrandStatus.REQUESTED);
        }

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));
            errand.setCategory(category);
        }

        errand.setLocation(dto.getLocation());
        errand.setPrice(dto.getPrice());
        errand.setEstimatedTime(dto.getEstimatedTime());
        errand.setDeadline(dto.getDeadline());
    }
}