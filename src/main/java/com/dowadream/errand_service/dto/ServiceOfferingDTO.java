package com.dowadream.errand_service.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ServiceOfferingDTO {
    private Long id;
    private String title;
    private String description;
    private BigDecimal priceRange;
    private String location;
    private Long categoryId;
    private Long providerId;
    private List<MultipartFile> images;
}