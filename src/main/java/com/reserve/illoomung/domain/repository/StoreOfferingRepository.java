package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.domain.entity.StoreOffering;
import com.reserve.illoomung.domain.entity.Stores;
import com.reserve.illoomung.domain.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreOfferingRepository extends JpaRepository<StoreOffering, Long> {
    List<StoreOffering> findByStoreStoreId(Long storeId);
    List<StoreOffering> findByStoreStoreIdAndStatus(Long storeId, Status status);
}
