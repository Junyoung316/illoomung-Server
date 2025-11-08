package com.reserve.illoomung.domain.entity.key;

import java.io.Serializable;
import java.util.Objects;

public class OfferingCategoryMappingId implements Serializable { // 상품/서비스 카테고리 복합키
    private Long offering;
    private Long category;

    public OfferingCategoryMappingId() {}

    public OfferingCategoryMappingId(Long offering, Long category) {
        this.offering = offering;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OfferingCategoryMappingId)) return false;
        OfferingCategoryMappingId that = (OfferingCategoryMappingId) o;
        return Objects.equals(offering, that.offering) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(offering, category);
    }
}