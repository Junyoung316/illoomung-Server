package com.reserve.illoomung.application.auth.register;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.reserve.illoomung.application.verification.oauth.kakao.KakaoService;
import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.enums.Role;
import com.reserve.illoomung.core.domain.entity.enums.SocialProvider;
import com.reserve.illoomung.core.domain.entity.enums.Status;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.dto.CryptoResult;
import com.reserve.illoomung.core.util.SecurityUtil;
import com.reserve.illoomung.domain.service.RegisterValidator;
import com.reserve.illoomung.dto.request.auth.register.LocalRegisterRequest;
import com.reserve.illoomung.dto.request.auth.register.SocialRegisterRequest;

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
    private final KakaoService kakaoService;;

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
        log.info("[회원가입] Account 저장 시도");
        Account savedAccount = accountRepository.save(account);
        log.info("[회원가입] 성공: accountId={}", savedAccount.getAccountId());
    }

    @Override
    @Transactional
    public void localRegister(LocalRegisterRequest request) { // 권한은 Role.USER로 고정
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

    private void kakaoRegister(String socialToken) {
        log.info("[회원가입] KAKAO 회원가입: {}", "KAKAO");
        String kakaoUserInfo = kakaoService.getKakaoUserInfo(socialToken);
        // TODO: 토큰 정보 검증 로직 추가 필요
        throw new UnsupportedOperationException("소셜 회원가입은 현재 지원되지 않습니다.");
        // 카카오 회원가입 로직을 구현합니다.
        // 소셜 토큰을 사용하여 카카오 API로부터 사용자 정보를 가져오고, 이를 기반으로 계정을 생성합니다.
        // 이 부분은 위의 socialRegister 메소드에서 처리됩니다.
    }

    @Override
    @Transactional
    public void socialRegister(SocialRegisterRequest request, String socialToken) {
        RegisterData socialData;
        log.info("[회원가입] 소셜 회원가입 시도: {}", request.getSocialProvider());
        log.info("[회원가입] 소셜 토큰: {}", socialToken);
        switch (request.getSocialProvider()) {
            case KAKAO -> {
                kakaoRegister(socialToken);
            }
            case NAVER -> {
                log.info("[회원가입] NAVER 회원가입: {}", request.getSocialProvider());
                // TODO: 네이버 소셜 회원가입 로직 구현 필요
                throw new UnsupportedOperationException("소셜 회원가입은 현재 지원되지 않습니다.");
            }
            case GOOGLE -> {
                log.info("[회원가입] GOOGLE 회원가입: {}", request.getSocialProvider());
                // TODO: 구글 소셜 회원가입 로직 구현 필요
                throw new UnsupportedOperationException("소셜 회원가입은 현재 지원되지 않습니다.");
            }
            default -> {
                log.error("[회원가입] 지원하지 않는 소셜 제공사: {}", request.getSocialProvider());
                throw new UnsupportedOperationException("지원하지 않는 소셜 제공사입니다: " + request.getSocialProvider());
            }
        }

    }

}
