package com.reserve.illoomung.domain.entity;

import com.reserve.illoomung.domain.entity.key.StoreCategoryMappingId;
import jakarta.persistence.*;

@Entity
@Table(name = "store_category_mappings")
@IdClass(StoreCategoryMappingId.class) // 복합 키 클래스를 따로 만듦
public class StoreCategoryMapping { // 업체 카테고리 매핑

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Stores store;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private StoreCategory category;

    // 생성자, getter, setter 생략
}