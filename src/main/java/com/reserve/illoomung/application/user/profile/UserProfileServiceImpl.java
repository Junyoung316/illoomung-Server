package com.reserve.illoomung.application.user.profile;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.UserProfile;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.domain.repository.UserProfileRepository;
import com.reserve.illoomung.core.dto.CryptoResult;
import com.reserve.illoomung.core.exception.LoginFailException;
import com.reserve.illoomung.core.util.SecurityUtil;
import com.reserve.illoomung.dto.request.auth.ChangePasswordRequest;
import com.reserve.illoomung.dto.request.auth.ProfileRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final SecurityUtil securityUtil;
    private final PasswordEncoder passwordEncoder;

    private final AccountRepository accountRepository;
    private final UserProfileRepository userProfileRepository;

    private Account userCheck() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authenticated: {}", authentication);
        if(authentication != null && authentication.isAuthenticated()) {
            String userId = authentication.getName();  // 사용자 식별자(ID) 조회
            log.info("authenticated id: {}", userId);

            Account account = accountRepository.findByAccountId(Long.valueOf(userId))
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
            log.info("authenticated account: {}", account);
            return account;
        }
        return null;
    }

    @Override
    @Transactional
    public void patchProfile(ProfileRequest request) {
        Account account = userCheck();

        UserProfile userProfile = userProfileRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("사용자의 프로필을 찾을 수 없습니다."));

        CryptoResult name = securityUtil.cryptoResult(request.getName());
        CryptoResult nickname = securityUtil.cryptoResult(request.getNickname());
        CryptoResult phone = securityUtil.cryptoResult(request.getPhone());

        userProfile.patchProfile(name, nickname, phone);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        Account account = userCheck();
        if(!(account == null)) {
            if(passwordEncoder.matches(request.getOldPassword(), account.getPasswordHash())) {
                if(request.getNewPassword().equals(request.getCheckNewPassword())) {
                    account.changePassword(passwordEncoder.encode(request.getNewPassword()));
                }
            }
        }
    }
}
