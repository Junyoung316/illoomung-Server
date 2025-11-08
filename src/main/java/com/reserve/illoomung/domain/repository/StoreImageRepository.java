package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
    Optional<StoreImage> findByStoreStoreId(Long storeId);
    List<StoreImage> findByStoreStoreIdIn(List<Long> store);
}
