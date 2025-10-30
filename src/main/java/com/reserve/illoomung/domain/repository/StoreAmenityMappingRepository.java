package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreAmenityMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreAmenityMappingRepository extends JpaRepository<StoreAmenityMapping, Long> {
    @Query("SELECT m FROM StoreAmenityMapping m " +
            "JOIN FETCH m.amenity " +
            "WHERE m.store.storeId IN :storeIds")
    List<StoreAmenityMapping> findAmenitiesByStoreStoreIdsIn(@Param("storeIds") List<Long> storeIds);
}
