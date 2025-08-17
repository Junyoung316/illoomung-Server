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
import com.reserve.illoomung.dto.request.auth.register.RegisterRequest;

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

    private String emailEncrypt = null;
    private String emailHash = null;
    private String passwordHash = null;
    private SocialProvider socialProvider = SocialProvider.NONE;
    private String socialId = null;
    private String socialIdHash = null;

    public void initData() {
        this.emailEncrypt = null;
        this.emailHash = null;
        this.passwordHash = null;
        this.socialProvider = SocialProvider.NONE;
        this.socialId = null;
        this.socialIdHash = null;
        log.info("[회원가입] 초기화 완료");
    }
    
    public void setSocial(String socialProvider, String socialId) {
        CryptoResult socialIdCryptoResult = securityUtil.cryptoResult(socialId);

        this.socialProvider = SocialProvider.valueOf(socialProvider);
        this.socialId = socialIdCryptoResult.encryptedData();
        this.socialIdHash = socialIdCryptoResult.hashedData();
        log.info("[회원가입] 소셜 정보 설정: socialProvider={}, socialId={}", this.socialProvider, this.socialId);
    }

    public void setLocal(String email, String password) {
        CryptoResult emailCryptoResult = securityUtil.cryptoResult(email);

        this.emailEncrypt = emailCryptoResult.encryptedData();
        this.emailHash = emailCryptoResult.hashedData();
        this.passwordHash = passwordEncoder.encode(password);
        log.info("[회원가입] 로컬 정보 설정: email={}, emailHash={}", this.emailEncrypt, this.emailHash);
    }
    
    @Override
    @Transactional
    public Account register(RegisterRequest request, Role role) {
        initData(); // 초기화
        log.info("[회원가입] 시도");
        switch (request.getSocialProvider()) {
            case "KAKAO" -> { // 카카오 소셜 회원가입
                log.info("[회원가입] KAKAO 회원가입: {}", request.getSocialProvider());

                // 소셜 제공사에서 발급한 엑세스 토큰을 클라이언트로부터 전달받은 후 
                // 소셜 제공사에 정보 요청을 통해 소셜 아이디를 획득
                // 소셜 아이디는 암호화 후 해시값을 저장

                // setSocial(request.getSocialProvider(), request.getSocialId());
                // registerValidator.validateSocialDuplicate(this.socialProvider, this.socialIdHash);
                return null; // TODO: 소셜 회원가입 로직 구현 필요
            }
            case "NAVER" -> { // 네이버 소셜 회원가입
                log.info("[회원가입] NAVER 회원가입: {}", request.getSocialProvider());
                
            }
            case "GOOGLE" -> { // 구글 소셜 회원가입
                log.info("[회원가입] GOOGLE 회원가입: {}", request.getSocialProvider());
                
            }
            default -> {
                // 로컬 회원가입
                log.info("[회원가입] 일반 회원가입: {}", request.getSocialProvider());
                socialProvider = SocialProvider.NONE;
                setLocal(request.getEmail(), request.getPassword());
                registerValidator.validateEmailDuplicate(this.emailHash);
            }
        }

        // 1. 계정 생성
        Account account = Account.builder()
                .email(emailEncrypt)
                .emailHash(emailHash)
                .passwordHash(passwordHash)
                .role(role)
                .socialProvider(socialProvider)
                .socialId(socialId)
                .socialIdHash(socialIdHash)
                .status(Status.ACTIVE)
                .build();
        log.info("[회원가입] Account 저장 시도: email={}, role={}", request.getEmail(), role);
        Account savedAccount = accountRepository.save(account);
        log.info("[회원가입] 성공: accountId={}, email={}", savedAccount.getAccountId(), request.getEmail());

        return savedAccount;
    }
}
