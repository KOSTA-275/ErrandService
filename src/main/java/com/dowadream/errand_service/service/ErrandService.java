package com.dowadream.errand_service.service;

import com.dowadream.errand_service.entity.Errand;
import com.dowadream.errand_service.repository.ErrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 심부름 관련 비즈니스 로직 처리클래스
 */
@Service
public class ErrandService {

    private final ErrandRepository errandRepository;

    @Autowired
    public ErrandService(ErrandRepository errandRepository) {
        this.errandRepository = errandRepository;
    }

    /**
     * 모든 심부름 정보를 조회
     * @return 전체 심부름 목록
     */
    public List<Errand> getAllErrands() {
        return errandRepository.findAll();
    }

    /**
     * 특정 ID의 심부름 정보를 조회합니다.
     * @param id 심부름 ID
     * @return 조회된 심부름 정보 (Optional)
     */
    public Optional<Errand> getErrandById(Long id) {
        return errandRepository.findById(id);
    }

    /**
     * 새로운 심부름을 생성합니다.
     * @param errand 생성할 심부름 정보
     * @return 생성된 심부름
     */
    public Errand createErrand(Errand errand) {
        return errandRepository.save(errand);
    }

    /**
     * 기존 심부름 정보를 수정합니다.
     * @param id 수정할 심부름 ID
     * @param errandDetails 수정할 심부름 정보
     * @return 수정된 심부름
     */
    public Errand updateErrand(Long id, Errand errandDetails) {
        Errand errand = errandRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("심부름을 찾을 수 없습니다"));

        errand.setTitle(errandDetails.getTitle());
        errand.setDescription(errandDetails.getDescription());
        errand.setStatus(errandDetails.getStatus());
        errand.setRequesterSeq(errandDetails.getRequesterSeq());
        errand.setRunnerSeq(errandDetails.getRunnerSeq());
        errand.setCategory(errandDetails.getCategory());

        return errandRepository.save(errand);
    }

    /**
     * 특정 심부름을 삭제합니다.
     * @param id 삭제할 심부름 ID
     */
    public void deleteErrand(Long id) {
        errandRepository.deleteById(id);
    }

    /**
     * 특정 카테고리에 속한 심부름을 조회합니다.
     * @param categoryId 카테고리 ID
     * @return 해당 카테고리의 심부름 목록
     */
    public List<Errand> getErrandsByCategory(Long categoryId) {
        return errandRepository.findAll().stream()
                .filter(errand -> errand.getCategory().getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }
}