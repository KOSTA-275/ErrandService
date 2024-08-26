package com.dowadream.errand_service.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ErrandDTO {
    private Long errandSeq;
    private String title;
    private String description;
    private Long requesterSeq;
    private Long runnerSeq;
    private String status;
    private Long categoryId;
    private List<MultipartFile> images;
}