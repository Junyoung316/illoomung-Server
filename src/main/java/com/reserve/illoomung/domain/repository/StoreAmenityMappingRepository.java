package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreAmenityMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreAmenityMappingRepository extends JpaRepository<StoreAmenityMapping, Long> {
}
