package com.reserve.illoomung.core.util.jwt.application;

import com.reserve.illoomung.core.dto.LoginResponse;
import com.reserve.illoomung.core.dto.TokenIatExp;

import java.util.Map;

public interface JwtService {
    LoginResponse generateTokens(String accountId); // Access Token, Refresh Token 통합 생성
    String generatorAccessToken(String accountId, Map<String, Object> claims); // Access Token 생성
    TokenIatExp generatorRefreshToken(String accountId); // Refresh Token 생성
    String extractAccountIdFromToken(String token); // Access 토큰에서 Account Id 추출
    <T> T extractFromToken(String token, String claim, Class<T> requiredType); // 토큰에서 필요한 Claim 추출
    boolean validateJwtToken(String token); // 토큰 유효성 검사(서명 및 만료 등)
    long getRemainingTime(String token);

    /**
     * 토큰이 블랙리스트에 있는지 확인
     * @param token 검사할 토큰
     * @return 블랙리스트에 있으면 true, 없으면 false
     */
    boolean isTokenBlacklisted(String token);
}
