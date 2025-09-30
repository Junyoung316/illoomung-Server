package com.reserve.illoomung.domain.entity;

import com.reserve.illoomung.domain.entity.key.StoreAmenityMappingId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "store_amenity_mappings")
@IdClass(StoreAmenityMappingId.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreAmenityMapping { // 편의시설 매핑

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_store_amenity_store"))
    private Stores store;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenity_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_store_amenity_amenity"))
    private Amenity amenity;

    // 생성자, getter, setter 생략
}