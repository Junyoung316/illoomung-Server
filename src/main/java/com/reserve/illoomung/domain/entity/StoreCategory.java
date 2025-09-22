package com.reserve.illoomung.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "store_categories",
        uniqueConstraints = @UniqueConstraint(columnNames = "category_name"))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreCategory { // 업체 카테고리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", length = 50, nullable = false, unique = true)
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private StoreCategory parentCategory;

    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL)
    @Builder.Default
    private List<StoreCategory> subCategories = new ArrayList<>();

    @Column(name = "icon_url", length = 200)
    private String iconUrl;

    @Column(name = "sort_order", nullable = false)
    @Builder.Default
    private Integer sortOrder = 0; //

    // 생성자, getter, setter 생략
}