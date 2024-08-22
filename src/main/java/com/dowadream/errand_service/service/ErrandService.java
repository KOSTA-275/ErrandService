package com.dowadream.errand_service.service;

import com.dowadream.errand_service.entity.Errand;
import com.dowadream.errand_service.repository.ErrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

/**
 * 심부름 서비스 클래스
 * 심부름과 관련된 비즈니스 로직 처리
 */
@Service
public class ErrandService {

    private final ErrandRepository errandRepository;
    //private final RestTemplate restTemplate;

    /**
     * ErrandService 생성자
     * @param errandRepository 심부름 리포지토리
     */
    @Autowired
    public ErrandService(ErrandRepository errandRepository/*, RestTemplate restTemplate*/) {
        this.errandRepository = errandRepository;
        //this.restTemplate = restTemplate;
    }

    /**
     * 모든 심부름을 조회
     * @return 심부름 목록
     */
    public List<Errand> getAllErrands() {
        return errandRepository.findAll();
    }

    /**
     * 특정 심부름 ID로 조회
     * @param id 심부름 ID
     * @return 조회된 심부름 (Optional)
     */
    public Optional<Errand> getErrandById(Long id) {
        return errandRepository.findById(id);
    }

    /**
     * 새로운 심부름 생성
     * @param errand 생성할 심부름 정보
     * @return 생성된 심부름
     */
    public Errand createErrand(Errand errand) {
        //ID들 받아오기
        /*
        Long requesterId = restTemplate.getForObject("http://user-service/api/users/requester", Long.class);
        Long runnerId = restTemplate.getForObject("http://user-service/api/users/runner", Long.class);
        errand.setRequesterSeq(requesterId);
        errand.setRunnerSeq(runnerId);
        */

        //로컬 테스트
        if (errand.getRequesterSeq() == null) {
            errand.setRequesterSeq(1L); //임시 요청자 ID
        }
        if (errand.getRunnerSeq() == null) {
            errand.setRunnerSeq(2L); //임시 실행자 ID
        }

        return errandRepository.save(errand);
    }

    /**
     * 심부름 수정
     * @param id 수정할 심부름 ID
     * @param errandDetails 수정할 심부름 정보
     * @return 수정된 심부름 (없으면 null)
     */
    public Errand updateErrand(Long id, Errand errandDetails) {
        Optional<Errand> errand = errandRepository.findById(id);
        if (errand.isPresent()) {
            Errand existingErrand = errand.get();
            existingErrand.setTitle(errandDetails.getTitle());
            existingErrand.setDescription(errandDetails.getDescription());
            existingErrand.setStatus(errandDetails.getStatus());
            existingErrand.setRunnerSeq(errandDetails.getRunnerSeq());
            return errandRepository.save(existingErrand);
        }
        return null;
    }

    /**
     * 특정 심부름 삭제
     * @param id 삭제할 심부름 ID
     */
    public void deleteErrand(Long id) {
        errandRepository.deleteById(id);
    }
}