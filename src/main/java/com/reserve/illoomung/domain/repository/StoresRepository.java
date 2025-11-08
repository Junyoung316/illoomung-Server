package com.reserve.illoomung.domain.repository;

import java.util.Optional;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.domain.entity.Stores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoresRepository extends JpaRepository<Stores, Long>, StoresRepositoryCustom {
    Optional<Stores> findAllByStoreId(Long id);
    Optional<Stores> findByStoreIdAndOwnerAccountId(Long StoreId, Long ownerAccountId);
    boolean existsByStoreNameAndAddressFullHashAndAddressDetailsHash(String name, String address, String addressDetails); // 이름과 주소 중복 검사(이름과 주소 두가지가 중복이면 같은 업체로 판단)
}
