package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.OfferingCategoryMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferingCategoryMappingRepository extends JpaRepository<OfferingCategoryMapping, Long> {
}
