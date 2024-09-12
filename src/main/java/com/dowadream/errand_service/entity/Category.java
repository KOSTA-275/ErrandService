package com.dowadream.errand_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 카테고리 엔티티 클래스
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString(exclude = {"serviceOfferings", "errands", "image"})
public class Category {
    /**
     * 카테고리 ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(name = "category_seq", sequenceName = "category_seq", allocationSize = 1)
    private Long categoryId;

    /**
     * 카테고리 이름
     */
    @Column(nullable = false, length = 50)
    private String name;

    /**
     * 카테고리 설명
     */
    @Column(length = 255)
    private String description;

    /**
     * 이 카테고리에 속한 서비스 제공 목록
     */
    @OneToMany(mappedBy = "category")
    private List<ServiceOffering> serviceOfferings = new ArrayList<>();

    /**
     * 이 카테고리에 속한 심부름 목록
     */
    @OneToMany(mappedBy = "category")
    private List<Errand> errands = new ArrayList<>();

    /**
     * 카테고리 이미지
     */
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "image_id")
    private Image image;

    /**
     * 카테고리 이미지 설정
     * @param image 설정할 이미지
     */
    public void setImage(Image image) {
        if (image != null) {
            image.setImageType(Image.ImageType.CATEGORY);
        }
        this.image = image;
    }
}