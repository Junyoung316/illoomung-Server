package com.reserve.illoomung.core.aop.login_attempts;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.LoginAttempts;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.domain.repository.LoginAttemptsRepository;
import com.reserve.illoomung.core.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginAttemptService {
    private final LoginAttemptsRepository loginAttemptRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void recordLoginAttempt(@Nullable Account account, String ip, String userAgent, boolean success, @Nullable String reason) {
        // login_Attempt 저장

        LoginAttempts loginAttempts = LoginAttempts.builder()
                .account(account)
                .attemptedAt(DateTimeUtils.now())
                .ipAddress(ip)
                .userAgent(userAgent)
                .success(success)
                .failReason(reason)
                .build();

        loginAttemptRepository.save(loginAttempts);

        // Account last_login_at 저장
        if (account != null) {
            Account aaccount = accountRepository.findByAccountId(account.getAccountId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
            if (aaccount != null) {
                aaccount.lastLoginAtUpdate();
            }
        }
    }
}
