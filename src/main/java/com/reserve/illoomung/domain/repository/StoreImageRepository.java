package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreImage;
import com.reserve.illoomung.domain.entity.Stores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface StoreImageRepository extends JpaRepository<StoreImage, Long> {
    List<StoreImage> findByStoreStoreIdIn(List<Long> store);
}
