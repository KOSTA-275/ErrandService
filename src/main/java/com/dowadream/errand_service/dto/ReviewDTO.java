package com.dowadream.errand_service.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private Long errandId;
    private Long serviceOfferingId;
    private Long reviewerId;
    private Integer rating;
    private String comments;
    private LocalDateTime createdDate;

    // Utility method to check if this review is for an Errand
    public boolean isErrandReview() {
        return errandId != null;
    }

    // Utility method to check if this review is for a ServiceOffering
    public boolean isServiceOfferingReview() {
        return serviceOfferingId != null;
    }
}