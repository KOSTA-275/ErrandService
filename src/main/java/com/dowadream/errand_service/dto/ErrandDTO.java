package com.dowadream.errand_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 심부름 정보를 전송하기 위한 데이터 전송 객체(DTO)
 */
@Data
public class ErrandDTO {
    /**
     * 심부름 고유 번호
     */
    private Long errandSeq;

    /**
     * 심부름 제목
     */
    private String title;

    /**
     * 심부름 설명
     */
    private String description;

    /**
     * 요청자 고유 번호
     */
    private Long requesterSeq;

    /**
     * 수행자 고유 번호
     */
    private Long runnerSeq;

    /**
     * 심부름 상태
     */
    private String status;

    /**
     * 카테고리 ID
     */
    private Long categoryId;

    /**
     * 요청자 닉네임
     */
    private String requesterNickname;

    /**
     * 수행자 닉네임
     */
    private String runnerNickname;

    /**
     * 생성 일시
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdDate;

    /**
     * 수정 일시
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedDate;

    /**
     * 위치
     */
    private String location;

    /**
     * 가격
     */
    private BigDecimal price;

    /**
     * 예상 소요 시간 (분)
     */
    private Integer estimatedTime;

    /**
     * 마감 기한
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deadline;

    /**
     * 이미지 경로 목록
     */
    private List<String> imagePaths;
}