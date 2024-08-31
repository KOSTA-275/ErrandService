package com.dowadream.errand_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "review_seq")
    @SequenceGenerator(name = "review_seq", sequenceName = "review_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "errand_seq")
    private Errand errand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_offering_id")
    private ServiceOffering serviceOffering;

    @Column(nullable = false)
    private Long reviewerId;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 500)
    private String comments;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    public void setErrand(Errand errand) {
        this.errand = errand;
        if (errand != null) {
            errand.getReviews().add(this);
        }
    }

    public void setServiceOffering(ServiceOffering serviceOffering) {
        this.serviceOffering = serviceOffering;
        if (serviceOffering != null) {
            serviceOffering.getReviews().add(this);
        }
    }
}