package com.reserve.illoomung.core.domain.repository;

import com.reserve.illoomung.core.domain.entity.RefreshTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokensRepository extends JpaRepository<RefreshTokens, Long> {

    boolean existsByToken(String token);
    boolean existsByTokenAndRevoked(String token, boolean revoked);

    @Modifying
    @Query("UPDATE RefreshTokens r SET r.revoked = false WHERE r.token = :token")
    Optional<Integer> revokeByToken(String token);

    @Modifying
    @Query("UPDATE RefreshTokens r SET r.revoked = false WHERE r.account.accountId = :accountId")
    int revokeByAccountId(@Param("accountId") Long accountId);



}
