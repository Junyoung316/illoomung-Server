package com.reserve.illoomung.application.auth.register;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.reserve.illoomung.application.verification.oauth.kakao.KakaoService;
import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.enums.Role;
import com.reserve.illoomung.core.domain.entity.enums.SocialProvider;
import com.reserve.illoomung.core.domain.entity.enums.Status;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.dto.CryptoResult;
import com.reserve.illoomung.core.exception.NoAuthorizationHeaderException;
import com.reserve.illoomung.core.util.SecurityUtil;
import com.reserve.illoomung.domain.service.RegisterValidator;
import com.reserve.illoomung.dto.request.auth.register.LocalRegisterRequest;
import com.reserve.illoomung.dto.request.auth.register.SocialRegisterRequest;
import com.reserve.illoomung.dto.response.verification.oauth.kakao.KakaoUserInfoResponse;

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
    ) { }
    
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

    private RegisterData kakaoRegister(String socialToken) {
        RegisterData kakaoData;
        log.info("[회원가입] KAKAO 회원가입: {}", "KAKAO");
        KakaoUserInfoResponse kakaoUserInfo = kakaoService.getKakaoUserInfo(socialToken); // 소셜 토큰으로 사용자 정보 조회 및 데이터 파싱
        kakaoUserInfo.getId();
        log.info("[회원가입] KAKAO ID: {}", kakaoUserInfo.getId());

        if (
            !(
                kakaoUserInfo.getKakaoAccount().isHasEmail() &&
                !kakaoUserInfo.getKakaoAccount().isAgeRangeNeedsAgreement() &&
                kakaoUserInfo.getKakaoAccount().isEmailValid() &&
                kakaoUserInfo.getKakaoAccount().isEmailVerified()
        )) { // 이메일이 없거나, 이메일 제공 동의가 안됐거나, 이메일이 유효하지 않거나, 이메일 인증이 안된 경우
            log.error("[회원가입] KAKAO 필수 정보 동의 필요 또는 이메일 없음");
            CryptoResult socialId = securityUtil.cryptoResult(String.valueOf(kakaoUserInfo.getId()));
            registerValidator.validateSocialDuplicate(SocialProvider.KAKAO, socialId.hashedData()); // 소셜 제공사 + 소셜 ID 중복 검증
            kakaoData = new RegisterData(
                null,
                null,
                null,
                SocialProvider.KAKAO,
                socialId.encryptedData(),
                socialId.hashedData()
            );

            return kakaoData;
            // TODO: 이메일 없이 회원가입 처리
        } else {
            CryptoResult email = securityUtil.cryptoResult(kakaoUserInfo.getKakaoAccount().getEmail());
            CryptoResult socialId = securityUtil.cryptoResult(String.valueOf(kakaoUserInfo.getId()));
            registerValidator.validateEmailDuplicate(email.hashedData()); // 이메일 중복 검증
            registerValidator.validateSocialDuplicate(SocialProvider.KAKAO, socialId.hashedData()); // 소셜 제공사 + 소셜 ID 중복 검증
            kakaoData = new RegisterData(
                email.encryptedData(),
                email.hashedData(),
                null,
                SocialProvider.KAKAO,
                socialId.encryptedData(),
                socialId.hashedData()
            );

            return kakaoData;
            // TODO: 소셜 id와 이메일로 회원가입 처리
        }

        // throw new UnsupportedOperationException("소셜 회원가입은 현재 지원되지 않습니다.");
        // 카카오 회원가입 로직을 구현합니다.
        // 소셜 토큰을 사용하여 카카오 API로부터 사용자 정보를 가져오고, 이를 기반으로 계정을 생성합니다.
        // 이메일, 소셜 ID 중복 검증 등을 수행합니다.
        // 이메일이 없는 경우, 소셜 ID로만 회원가입을 처리합니다. KakaoUserInfoDTO.md 참고
    }

    private RegisterData naverRegister(String socialToken) {
        RegisterData naverData;
        log.info("[회원가입] NAVER 회원가입: {}", "NAVER");
        throw new UnsupportedOperationException("소셜 회원가입은 현재 지원되지 않습니다.");
    }

    private RegisterData googleRegister(String socialToken) {
        RegisterData googleData;
        log.info("[회원가입] GOOGLE 회원가입: {}", "GOOGLE");
        throw new UnsupportedOperationException("소셜 회원가입은 현재 지원되지 않습니다.");
    }

    @Override
    @Transactional
    public void socialRegister(SocialRegisterRequest request, String socialToken) {
        RegisterData socialData;
        log.info("[회원가입] 소셜 회원가입 시도: {}", request.getSocialProvider());
        log.info("[회원가입] 소셜 토큰: {}", socialToken);

        if (!StringUtils.hasText(socialToken) || request.getSocialProvider() == SocialProvider.NONE) { // 토큰이 비어있거나 null인 경우 또는 소셜 제공사가 NONE인 경우
            log.error("[회원가입] 소셜 토큰 또는 소셜 제공사가 비어있음"); // 401 Unauthorized
            throw new NoAuthorizationHeaderException("[회원가입] 소셜 토큰 또는 소셜 제공사가 비어있음");
        }

        switch (request.getSocialProvider()) {
            case KAKAO -> {
                socialData = kakaoRegister(socialToken);
                createAccount(socialData);
            }
            case NAVER -> {
                naverRegister(socialToken);
            }
            case GOOGLE -> {
                googleRegister(socialToken);
            }
            default -> {
                log.error("[회원가입] 지원하지 않는 소셜 제공사: {}", request.getSocialProvider());
                throw new UnsupportedOperationException("지원하지 않는 소셜 제공사입니다: " + request.getSocialProvider());
            }
        }

    }

}
