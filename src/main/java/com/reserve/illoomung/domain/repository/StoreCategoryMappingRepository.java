package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreCategoryMappingRepository extends JpaRepository<StoreCategoryMapping, Long> {
}
