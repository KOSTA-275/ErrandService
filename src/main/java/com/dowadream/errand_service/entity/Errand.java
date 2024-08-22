package com.dowadream.errand_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "errands")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Errand {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "errand_seq_generator")
    @SequenceGenerator(name = "errand_seq_generator", sequenceName = "errand_seq", allocationSize = 1)
    @Column(name = "errand_seq")
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
}