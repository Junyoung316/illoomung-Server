package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {
}
