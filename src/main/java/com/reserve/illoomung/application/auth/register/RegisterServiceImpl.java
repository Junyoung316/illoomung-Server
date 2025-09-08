package com.reserve.illoomung.application.auth.register;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.enums.Role;
import com.reserve.illoomung.core.domain.entity.enums.SocialProvider;
import com.reserve.illoomung.core.domain.entity.enums.Status;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.dto.CryptoResult;
import com.reserve.illoomung.core.util.SecurityUtil;
import com.reserve.illoomung.domain.service.RegisterValidator;
import com.reserve.illoomung.dto.request.auth.LocalRegisterLoginRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final SecurityUtil securityUtil;
    private final RegisterValidator registerValidator;

    private record RegisterData(
            String emailEncrypt,
            String emailHash,
            String passwordHash,
            SocialProvider socialProvider,
            String socialId,
            String socialIdHash
    ) {
    }

    private void createAccount(RegisterData data) {
        // 1. 계정 생성
        Account account = Account.builder()
                .email(data.emailEncrypt)
                .emailHash(data.emailHash)
                .passwordHash(data.passwordHash)
                .role(Role.USER) // 기본 역할은 USER로 설정
                .socialProvider(data.socialProvider)
                .socialId(data.socialId)
                .socialIdHash(data.socialIdHash)
                .status(Status.ACTIVE)
                .build();
        log.debug("[회원가입] Account 저장 시도");
        Account savedAccount = accountRepository.save(account);
        log.info("[회원가입] 성공: accountId={}", savedAccount.getAccountId());
    }

    @Override
    @Transactional
    public void localRegister(LocalRegisterLoginRequest request) { // 권한은 Role.USER로 고정
        RegisterData localData;
        log.info("[회원가입] 로컬 회원가입 시도: {}", request.getEmail());
        CryptoResult email = securityUtil.cryptoResult(request.getEmail());
        registerValidator.validateEmailDuplicate(email.hashedData());
        localData = new RegisterData(
                email.encryptedData(),
                email.hashedData(),
                passwordEncoder.encode(request.getPassword()),
                SocialProvider.NONE,
                null,
                null
        );
        createAccount(localData);
        log.info("[회원가입] 로컬 회원가입 성공: {}", request.getEmail());
    }
}
