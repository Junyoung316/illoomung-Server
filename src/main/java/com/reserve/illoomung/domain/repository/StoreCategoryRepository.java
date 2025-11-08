package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {
    List<StoreCategory> findByCategoryNameIn(List<String> CategoryNames);
}
