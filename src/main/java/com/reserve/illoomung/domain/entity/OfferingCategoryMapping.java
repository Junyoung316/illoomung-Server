package com.reserve.illoomung.domain.entity;

import com.reserve.illoomung.domain.entity.key.OfferingCategoryMappingId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "offering_category_mappings")
@IdClass(OfferingCategoryMappingId.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfferingCategoryMapping { // 상품/서비스 카테고리 매핑

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offering_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_offering_category_offering"))
    private StoreOffering offering;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_offering_category_category"))
    private OfferingCategory category;

    // 생성자, getter, setter 생략
}