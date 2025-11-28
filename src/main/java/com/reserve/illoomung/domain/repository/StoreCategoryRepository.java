package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory, Long> {
    Optional<StoreCategory> findAllByCategoryId(Long categoryId);
    List<StoreCategory> findByCategoryNameIn(List<String> CategoryNames);

    @Query("SELECT COALESCE(MAX(c.sortOrder), 0) FROM StoreCategory c")
    Integer findMaxSortOrder();
}
