package com.reserve.illoomung.core.domain.repository;

import com.reserve.illoomung.core.domain.entity.LoginAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptsRepository extends JpaRepository<LoginAttempts, Long> {
}
