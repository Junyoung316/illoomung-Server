package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreOfferingRepository extends JpaRepository<StoreOffering, Long> {
}
