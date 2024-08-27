package com.dowadream.errand_service.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "serviceOffering", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
        image.setServiceOffering(this);
        image.setImageType(Image.ImageType.SERVICE_OFFERING);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setServiceOffering(null);
    }
}