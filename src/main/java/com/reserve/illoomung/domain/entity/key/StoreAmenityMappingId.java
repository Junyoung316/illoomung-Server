package com.reserve.illoomung.domain.entity.key;

import java.io.Serializable;
import java.util.Objects;

public class StoreAmenityMappingId implements Serializable { // 편의시설 복합키
    private Long store;
    private Long amenity;

    public StoreAmenityMappingId() {}

    public StoreAmenityMappingId(Long store, Long amenity) {
        this.store = store;
        this.amenity = amenity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreAmenityMappingId that)) return false;
        return Objects.equals(store, that.store) &&
                Objects.equals(amenity, that.amenity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, amenity);
    }
}