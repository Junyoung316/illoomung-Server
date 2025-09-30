package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.Stores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoresRepository extends JpaRepository<Stores, Long> {

    boolean existsByStoreNameAndAddressFullHashAndAddressDetailsHash(String name, String address, String addressDetails); // 이름과 주소 중복 검사(이름과 주소 두가지가 중복이면 같은 업체로 판단)
}
