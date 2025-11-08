package com.reserve.illoomung.core.domain.repository;

import com.reserve.illoomung.core.domain.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reserve.illoomung.core.domain.entity.UserProfile;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    boolean existsByNicknameHash(String nicknameHash);
    UserProfile findByAccountId(Account accountId);
    
}
