package com.dowadream.errand_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "service_offerings")
@Getter
@Setter
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

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "serviceOffering", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @Column(nullable = false)
    private Integer completedTasks = 0;

    public void addImage(Image image) {
        images.add(image);
        image.setServiceOffering(this);
        image.setImageType(Image.ImageType.SERVICE_OFFERING);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setServiceOffering(null);
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.setServiceOffering(this);
    }

    public void removeReview(Review review) {
        reviews.remove(review);
        review.setServiceOffering(null);
    }

    public void incrementCompletedTasks() {
        this.completedTasks++;
    }
}