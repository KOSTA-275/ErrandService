package com.dowadream.errand_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "errands")
@Data
public class Errand {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "errand_seq")
    @SequenceGenerator(name = "errand_seq", sequenceName = "errand_seq", allocationSize = 1)
    private Long errandSeq;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(name = "requester_seq")
    private Long requesterSeq;

    @Column(name = "runner_seq")
    private Long runnerSeq;

    @Column(nullable = false, length = 20)
    private String status;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "errand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
        image.setErrand(this);
    }

    public void removeImage(Image image) {
        images.remove(image);
        image.setErrand(null);
    }
}