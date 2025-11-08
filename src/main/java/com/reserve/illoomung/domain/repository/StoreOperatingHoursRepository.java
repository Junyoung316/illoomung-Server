package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreOperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreOperatingHoursRepository extends JpaRepository<StoreOperatingHours, Long> {
    List<StoreOperatingHours> findByStoreStoreIdOrderByDayOfWeekAsc(@Param("storeId") Long storeId);
    List<StoreOperatingHours> findByStoreStoreIdInAndDayOfWeekIn(List<Long> storeIds, List<Integer> dayOfWeekValues);
}
