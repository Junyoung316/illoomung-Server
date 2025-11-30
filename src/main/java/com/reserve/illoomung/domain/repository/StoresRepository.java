package com.reserve.illoomung.domain.repository;

import java.util.Optional;
import java.util.List;

import com.reserve.illoomung.domain.entity.Stores;
import com.reserve.illoomung.domain.entity.enums.StoreStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoresRepository extends JpaRepository<Stores, Long>, StoresRepositoryCustom {
    Optional<Stores> findAllByStoreId(Long id);
    Optional<List<Stores>> findAllByOwnerAccountIdStatus(Long id, StoreStatus status);
    Optional<Stores> findByStoreIdAndOwnerAccountId(Long StoreId, Long ownerAccountId);
    Optional<List<Stores>> findByStatus(StoreStatus status);
    boolean existsByStoreNameAndAddressFullHashAndAddressDetailsHash(String name, String address, String addressDetails); // 이름과 주소 중복 검사(이름과 주소 두가지가 중복이면 같은 업체로 판단)
}
