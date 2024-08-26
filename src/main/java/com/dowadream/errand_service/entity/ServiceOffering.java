package com.dowadream.errand_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "service_offerings")
@Data
public class ServiceOffering {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "service_offering_seq")
    @SequenceGenerator(name = "service_offering_seq", sequenceName = "service_offering_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private BigDecimal priceRange;

    @Column(nullable = false)
    private String location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false)
    private Long providerId;
}