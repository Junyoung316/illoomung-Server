package com.reserve.illoomung.domain.entity.key;

import java.io.Serializable;
import java.util.Objects;

public class StoreCategoryMappingId implements Serializable { // 업체 카테고리 복합키
    private Long store;
    private Long category;

    public StoreCategoryMappingId() {}

    public StoreCategoryMappingId(Long store, Long category) {
        this.store = store;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreCategoryMappingId that)) return false;
        return Objects.equals(store, that.store) &&
                Objects.equals(category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(store, category);
    }
}