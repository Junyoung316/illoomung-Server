package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreOffering;
import com.reserve.illoomung.domain.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreOfferingRepository extends JpaRepository<StoreOffering, Long> {
    Optional<StoreOffering> findAllByOfferingId(Long offeringId);
    List<StoreOffering> findByStoreStoreIdAndStatus(Long storeId, Status status);
}
