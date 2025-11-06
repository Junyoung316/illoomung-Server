package com.reserve.illoomung.application.auth.register;

import com.reserve.illoomung.core.domain.entity.UserProfile;
import com.reserve.illoomung.core.domain.repository.UserProfileRepository;
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
import com.reserve.illoomung.dto.request.auth.LocalRegisterRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final UserProfileRepository userProfileRepository;
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

    private record RegisterProfileData(
            Account accountId,
            String name,
            String nickname,
            String nicknameHash

    ) {
    }

    private Account createAccount(RegisterData data) {
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
        return savedAccount;
    }

    private void createUserProfile(RegisterProfileData data) {
        UserProfile userProfile = UserProfile.builder()
                .accountId(data.accountId)
                .name(data.name)
                .nickName(data.nickname)
                .nicknameHash(data.nicknameHash)
                .build();

        userProfileRepository.save(userProfile);
    }

    @Override
    @Transactional
    public void localRegister(LocalRegisterRequest request) { // 권한은 Role.USER로 고정
        RegisterData localData;
        log.info("[회원가입] 로컬 회원가입 시도: {}", request.getEmail());

        CryptoResult email = securityUtil.cryptoResult(request.getEmail());
        registerValidator.validateEmailDuplicate(email.hashedData());

        RegisterProfileData profileData;
        CryptoResult name = securityUtil.cryptoResult(request.getName());
        CryptoResult nickname = securityUtil.cryptoResult(request.getNickname());

        if(!request.getPassword().equals(request.getCheckPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (userProfileRepository.existsByNicknameHash(nickname.hashedData())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        localData = new RegisterData(
                email.encryptedData(),
                email.hashedData(),
                passwordEncoder.encode(request.getPassword()),
                SocialProvider.NONE,
                null,
                null
        );
        Account saveAccount = createAccount(localData);

        profileData = new RegisterProfileData(
                saveAccount,
                name.encryptedData(),
                nickname.encryptedData(),
                nickname.hashedData()
        );
        createUserProfile(profileData);
        log.info("[회원가입] 로컬 회원가입 성공: {}", request.getEmail());
    }
}
