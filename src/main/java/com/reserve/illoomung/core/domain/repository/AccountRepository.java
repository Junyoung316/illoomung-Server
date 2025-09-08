package com.reserve.illoomung.core.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.enums.SocialProvider;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmailHash(String email); // JPA 사용시 구현체 자동 생성 필요시 Impl 작성
    Account findByAccountId(Long accountId);
    boolean existsBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
    Optional<Long> findAccountIdBySocialProviderAndSocialId(SocialProvider socialProvider, String socialId);
}
