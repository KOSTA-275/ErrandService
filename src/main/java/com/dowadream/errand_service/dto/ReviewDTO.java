package com.dowadream.errand_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private Long errandId;
    private Long reviewerId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdDate;

    //필요에 따라 추가 메서드 구현

}