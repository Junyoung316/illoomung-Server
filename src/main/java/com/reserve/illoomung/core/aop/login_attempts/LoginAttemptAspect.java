package com.reserve.illoomung.core.aop.login_attempts;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.dto.CryptoResult;
import com.reserve.illoomung.core.util.RequestInfoUtil;
import com.reserve.illoomung.core.util.SecurityUtil;
import com.reserve.illoomung.dto.request.auth.LocalRegisterLoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LoginAttemptAspect {

    private final LoginAttemptService loginHistoryService;
    private final AccountRepository accountRepository;
    private final SecurityUtil securityUtil;

    // 로그인 관련 컨트롤러 메서드 실행 후 호출 (성공 시)
    @AfterReturning(pointcut = "execution(* com.reserve.illoomung.presentation.auth.login.LoginController.*(..))", returning = "result")
    public void afterLoginSuccess(JoinPoint joinPoint, Object result) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs != null ? attrs.getRequest() : null;

        String ip = "unknown";
        String userAgent = "unknown";
        if (request != null) {
            ip = RequestInfoUtil.extractClientIp(request);
            userAgent = RequestInfoUtil.extractUserAgent(request);
        }

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof LocalRegisterLoginRequest) {
                String email = ((LocalRegisterLoginRequest) arg).getEmail();
                CryptoResult emailCryptoResult = securityUtil.cryptoResult(email);
                String emailHash = emailCryptoResult.hashedData();
                // 이메일로 Account 조회
                Optional<Account> accountOpt = accountRepository.findByEmailHash(emailHash);
                Account account = accountOpt.orElse(null);
                if (accountOpt.isPresent()) {
                    log.info("[AOP][로그인] 성공 기록 저장: ip={}, email={}", ip, email);
                } else {
                    log.warn("[AOP][로그인] 로그인 성공했으나 Account를 찾지 못함: email={}", email);
                }
                loginHistoryService.recordLoginAttempt(account, ip, userAgent, true, null);
            }
        }
    }

    // 로그인 실패 시 예외 발생하면 실행 (실패 기록)
    @AfterThrowing(pointcut = "execution(* com.reserve.illoomung.presentation.auth.login.LoginController.*(..))", throwing = "ex")
    public void afterLoginFailure(JoinPoint joinPoint, Throwable ex) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs != null ? attrs.getRequest() : null;

        String ip = "unknown";
        String userAgent = "unknown";
        if (request != null) {
            ip = RequestInfoUtil.extractClientIp(request);
            userAgent = RequestInfoUtil.extractUserAgent(request);
        }

        String failReason = (ex != null && ex.getMessage() != null) ? ex.getMessage() : "Unknown error";

        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof LocalRegisterLoginRequest) {
                String email = ((LocalRegisterLoginRequest) arg).getEmail();
                CryptoResult emailCryptoResult = securityUtil.cryptoResult(email);
                String emailHash = emailCryptoResult.hashedData();
                // 이메일로 Account 조회
                Optional<Account> accountOpt = accountRepository.findByEmailHash(emailHash);
                Account account = accountOpt.orElse(null);

                if (account != null) {
                    log.info("[AOP][로그인] 실패 기록 저장: ip={}, email={}, reason={}", ip, email, failReason);
                } else {
                    log.warn("[AOP][로그인] 로그인 실패했으나 Account를 찾지 못함: email={}", email);
                }

                // 실패 이유도 같이 기록
                loginHistoryService.recordLoginAttempt(account, ip, userAgent, false, failReason);
            }
        }
    }
}
