package com.dowadream.errand_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 심부름 데이터 전송 객체(DTO)
 * 클라이언트와 서버 간의 심부름 정보 교환
 */
@Data
public class ErrandDTO {
    private Long errandSeq;             //심부름 고유 번호
    private String title;               //심부름 제목
    private String description;         //심부름 설명
    private Long requesterSeq;          //요청자 고유 번호
    private Long runnerSeq;             //수행자 고유 번호
    private String status;              //심부름 상태
    private Long categoryId;            //카테고리 ID
    private String requesterNickname;   //요청자 닉네임
    private String runnerNickname;      //수행자 닉네임

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdDate; //생성 일시

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedDate; //수정 일시

    private String location;         //위치
    private BigDecimal price;        //가격
    private Integer estimatedTime;   //예상 소요 시간 (분)

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deadline;  //마감 기한

    private List<String> imagePaths; //이미지 경로 목록
}