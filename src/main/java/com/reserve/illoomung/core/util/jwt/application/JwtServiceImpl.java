package com.reserve.illoomung.core.util.jwt.application;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.RefreshTokens;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.domain.repository.RefreshTokensRepository;
import com.reserve.illoomung.core.domain.repository.TokenBlacklistRepository;
import com.reserve.illoomung.core.dto.LoginResponse;
import com.reserve.illoomung.core.dto.TokenIatExp;
import com.reserve.illoomung.core.exception.CustomJwtException;
import com.reserve.illoomung.core.util.DateTimeUtils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
//@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

//    @Value("${com.reserve.illoomung.jwt.service.secret-key}")
//    private String secretKey;

    private final SecretKey key;
    private final AccountRepository accountRepository;
    private final RefreshTokensRepository refreshTokensRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;


    @Value("${jwt.access-token-expire-ms}")
    private int accessTokenExpireMs;

    @Value("${jwt.refresh-token-expire-ms}")
    private long refreshTokenExpireMs;

//    @Value("${redis.refresh-token-expire-seconds}")
//    private int redisRefreshTokenExpireSeconds;

    public JwtServiceImpl(@Value("${com.reserve.illoomung.jwt.service.secret-key}") String secretKey,
                          AccountRepository accountRepository,
                          RefreshTokensRepository refreshTokensRepository,
                          TokenBlacklistRepository tokenBlacklistRepository) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accountRepository = accountRepository;
        this.refreshTokensRepository = refreshTokensRepository;
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    @Transactional
    public boolean toggleRevokedByToken(String refreshToken) { // 토큰 값을 기준으로 폐기
        Optional<Integer> updatedRows = refreshTokensRepository.revokeByToken(refreshToken);
        return updatedRows.orElse(0) > 0;
    }

    public boolean revokeTokensByAccountId(Long accountId) {
        int updatedRows = refreshTokensRepository.revokeByAccountId(accountId);
        return updatedRows > 0;
    }


    @Override
    @Transactional
    public LoginResponse generateTokens(String accountId) { // Access Token, Refresh Token 통합 생성
        // TODO: 기존 리프레시 토큰 삭제 --
        // 토큰 정보 테이블에 refresh token 등록
        // 로그아웃 등 이벤트 발생 시 토큰 정보 테이블에 해당 토큰의 폐지 여부를 True로 변경 후 블랙리스트 등록

        boolean success = revokeTokensByAccountId(Long.valueOf(accountId)); // 토큰 정보 테이블에 사용자의 기존 Refresh Token 폐기

        if (!success) {
            // TODO: 토큰 폐기 실패 시 커스텀 예외 던지기
            // throw new TokenRevokeException("토큰 폐기에 실패했습니다. accountId=" + accountId);
        }

        Account account = accountRepository.findByAccountId(Long.valueOf(accountId));
        String accessToken = generatorAccessToken(
                accountId, Map.of("role", account.getRole())
        );
        TokenIatExp refreshTokenInfo = generatorRefreshToken(accountId);
        Long expiresAt = DateTimeUtils.instantToEpochSeconds(refreshTokenInfo.expiresAt());
        log.info("[재발급] JWT 토큰 발급 완료: accountId={}, role={}", accountId, account.getRole());
        log.info("[JWT 토큰] 생성={}, 만료={}L", DateTimeUtils.krZonedDateTime(refreshTokenInfo.issuedAt()), expiresAt);

        RefreshTokens tokenInfo = RefreshTokens.builder()
                .account(account)
                .token(refreshTokenInfo.token())
                .expiresAt(expiresAt)
                .createdAt(refreshTokenInfo.issuedAt())
                .revoked(true)
                .build();

        refreshTokensRepository.save(tokenInfo);
        // 사용자 기본키, 사용자 토큰 값, 만료, 생성, 폐기 여부
        return new LoginResponse(accessToken, refreshTokenInfo.token());
    }

    @Override
    public String generatorAccessToken(String accountId, Map<String, Object> claims) { // Access Token 생성
        Instant now = DateTimeUtils.now();
        Instant expiration = now.plusMillis(accessTokenExpireMs);
        Date issuedAt = DateTimeUtils.instantToDate(now);
        Date expiresAt = DateTimeUtils.instantToDate(expiration);

        String token = Jwts.builder()
                .subject(accountId)
                .claims(claims)
                .claim("tokenType", "access")
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .signWith(key)
                .compact();
        long accessTokenExpireMinutes = accessTokenExpireMs / 1000 / 60;
        log.info("[JWT] 토큰 생성: accountId={}, 생성={}, role={}, 만료={}, {}ms ({}분)", accountId, DateTimeUtils.krZonedDateTime(now), claims.get("role"), DateTimeUtils.krZonedDateTime(expiration), accessTokenExpireMs, accessTokenExpireMinutes);
        return token;
    }

    @Override
    public TokenIatExp generatorRefreshToken(String accountId) {// Refesh Token 생성
        Instant now = DateTimeUtils.now();
        Instant expiration = now.plusMillis(refreshTokenExpireMs);
        Date issuedAt = DateTimeUtils.instantToDate(now);
        Date expiresAt = DateTimeUtils.instantToDate(expiration);

        String token = Jwts.builder()
                .subject(accountId)
                .claim("tokenType", "refresh")
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .signWith(key)
                .compact();
        long refreshTokenExpireMinutes = refreshTokenExpireMs / 1000 / 60;
        long refreshTokenExpireDay = refreshTokenExpireMinutes / 60 / 24;
        log.info("[JWT] 토큰 생성: accountId={}, tokenType=refresh, 생성={}, 만료={}, {}ms ({}분,  {}일)", accountId, DateTimeUtils.krZonedDateTime(now), DateTimeUtils.krZonedDateTime(expiration), refreshTokenExpireMs, refreshTokenExpireMinutes, refreshTokenExpireDay);
        return new TokenIatExp(token, now, expiration);
    }

    @Override
    public String extractAccountIdFromToken(String token) { // Access 토큰에서 Account Id 추출
        try {
            String accountId = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            log.info("[JWT] 토큰 파싱: accountId={} from token", accountId);
            return accountId;
        } catch (ExpiredJwtException e) {
            throw new CustomJwtException("만료된 JWT 토큰입니다.", e);
        } catch (SignatureException e) {
            throw new CustomJwtException("JWT 서명 검증에 실패했습니다.", e);
        } catch (MalformedJwtException e) {
            throw new CustomJwtException("잘못된 형식의 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            throw new CustomJwtException("지원하지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            throw new CustomJwtException("JWT 토큰이 null이거나 비어 있습니다.", e);
        } catch (JwtException e) {
            throw new CustomJwtException("JWT 처리 중 알 수 없는 예외가 발생했습니다.", e);
        }
    }

    @Override
    public <T> T extractFromToken(String token, String claim, Class<T> requiredType) {
        try {
            Object value = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .get(claim);

            if (value == null) {
                return null;
            }

            if (requiredType == String.class) {
                return requiredType.cast(value.toString());
            } else if (requiredType == Long.class) {
                if (value instanceof Number) {
                    return requiredType.cast(((Number) value).longValue());
                } else if (value instanceof String) {
                    return requiredType.cast(Long.valueOf((String) value));
                }
            } else if (requiredType.isInstance(value)) {
                return requiredType.cast(value);
            }

            throw new IllegalArgumentException("지원하지 않는 클레임 타입 변환입니다.");

        } catch (ExpiredJwtException e) {
            throw new CustomJwtException("만료된 JWT 토큰입니다.", e);
        } catch (SignatureException e) {
            throw new CustomJwtException("JWT 서명 검증에 실패했습니다.", e);
        } catch (MalformedJwtException e) {
            throw new CustomJwtException("잘못된 형식의 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            throw new CustomJwtException("지원하지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            throw new CustomJwtException("JWT 토큰이 null이거나 비어 있습니다.", e);
        } catch (JwtException e) {
            throw new CustomJwtException("JWT 처리 중 알 수 없는 예외가 발생했습니다.", e);
        }
    }


    @Override
    public boolean isTokenBlacklisted(String token) {
        try {
            // TODO: Redis에서 블랙리스트 확인
//            String blacklistStatus = redisService.getData("blacklist", token);
//            return "logout".equals(blacklistStatus);
            return true;
        } catch (Exception e) {
            log.error("[JWT] 블랙리스트 확인 중 오류 발생: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean validateJwtToken(String token) { // 토큰 유효성 검사(서명 및 만료 등)
        try {
            // 1. 블랙리스트 확인
            if (!isTokenBlacklisted(token)) {
                log.warn("[JWT] 블랙리스트에 등록된 토큰입니다: token={}", token);
                throw new CustomJwtException("로그아웃된 토큰입니다.");
            }

            // 2. 토큰 유효성 검증
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            log.info("[JWT] 토큰 검증 성공: token={}", token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new CustomJwtException("만료된 JWT 토큰입니다.", e);
        } catch (SignatureException e) {
            throw new CustomJwtException("JWT 서명 검증에 실패했습니다.", e);
        } catch (MalformedJwtException e) {
            throw new CustomJwtException("잘못된 형식의 JWT 토큰입니다.", e);
        } catch (UnsupportedJwtException e) {
            throw new CustomJwtException("지원하지 않는 JWT 토큰입니다.", e);
        } catch (IllegalArgumentException e) {
            throw new CustomJwtException("JWT 토큰이 null이거나 비어 있습니다.", e);
        } catch (JwtException e) {
            throw new CustomJwtException("JWT 처리 중 알 수 없는 예외가 발생했습니다.", e);
        }
    }

    @Override
    public long getRemainingTime(String token) {
        try {
            // 토큰 파싱 및 서명 검증 (만료된 토큰이면 ExpiredJwtException 발생)
            Date expiration = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getExpiration();

            long now = System.currentTimeMillis();
            return expiration.getTime() - now;
        } catch (ExpiredJwtException e) {
            // 이미 만료된 토큰
            return 0;
        } catch (Exception e) {
            // 기타 파싱 오류
            return 0;
        }
    }
}
