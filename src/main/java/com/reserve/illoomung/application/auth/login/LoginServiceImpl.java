package com.reserve.illoomung.application.auth.login;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.enums.Status;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.dto.CryptoResult;
import com.reserve.illoomung.core.dto.LoginResponse;
import com.reserve.illoomung.core.exception.LoginFailException;
import com.reserve.illoomung.core.util.SecurityUtil;
import com.reserve.illoomung.core.util.jwt.application.JwtService;
import com.reserve.illoomung.dto.request.auth.LocalLoginRequest;
import com.reserve.illoomung.dto.request.auth.SocialRegisterLoginRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtil securityUtil;
    private final JwtService jwtService;

    @Override
    public LoginResponse localLogin(LocalLoginRequest request){

        log.info("[로그인] 시도: email={}", request.getEmail());

        CryptoResult emailCryptoResult = securityUtil.cryptoResult(request.getEmail());
        String emailHash = emailCryptoResult.hashedData();

        Account account = null;
        try {
            account = accountRepository.findByEmailHash(emailHash)
                    .orElseThrow(() -> {
                        log.warn("[로그인] 계정 없음: email={}", request.getEmail());
                        return new LoginFailException(null, "존재하지 않는 계정");
                    });
        } catch (Exception e) {
            log.error("[로그인] 예외: {}", e.getMessage());
            throw e;
        }

        if (!passwordEncoder.matches(request.getPassword(), account.getPasswordHash())) {
            log.warn("[로그인] 비밀번호 불일치: email={}", request.getEmail());
            throw new LoginFailException(account, "비밀번호 불일치" );
        }

        if (account.getStatus() != Status.ACTIVE) {
            log.warn("[로그인] 계정 비활성화: email={}", request.getEmail());
            throw new LoginFailException(account, "계정 비활성화" );
        }
        log.info("[로그인] 성공: accountId={}, email={}", account.getAccountId(), request.getEmail());

        // jwt 토큰 생성 및 반환
        return jwtService.generateTokens(String.valueOf(account.getAccountId()));
    }

    @Override
    public void socialLogin(SocialRegisterLoginRequest request, Long accountId) {
        // TODO: regiser 서비스에서 이미 가입된 사용자를 로그인 처리 로직 구현
    }

}
