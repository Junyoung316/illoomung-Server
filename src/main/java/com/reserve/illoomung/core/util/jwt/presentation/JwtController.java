package com.reserve.illoomung.core.util.jwt.presentation;

import com.reserve.illoomung.core.domain.repository.RefreshTokensRepository;
import com.reserve.illoomung.core.dto.LoginResponse;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.core.exception.InvalidTokenTypeException;
import com.reserve.illoomung.core.util.jwt.application.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
class JwtController {

    private final JwtService jwtService;
    private final RefreshTokensRepository refreshTokensRepository;

    @PostMapping("/refresh") // TODO: 재발급 로직에서 데이터베이스 폐기 여부 및 토큰값 검증 추가
    public ResponseEntity<MainResponse<LoginResponse>> refreshToken(@RequestHeader("refresh-token") String refreshToken) {
        String token = null;
        if (refreshToken != null) {
            token = refreshToken;
        }
        boolean isRevoked = refreshTokensRepository.existsByTokenAndRevoked(token, true); // 토큰이 폐기 상태인지 확인
        log.info("Refresh token Revoked: {}", isRevoked);
        log.info("[Token]: {}", token);
        String tokenType = jwtService.extractFromToken(token, "tokenType", String.class);
        if(!tokenType.equals("refresh")) {
            throw new InvalidTokenTypeException("Refresh 토큰이 아닙니다.");
        }
        // 리프레시 토큰 유효성 검사
        boolean val = jwtService.validateJwtToken(token);
        log.info("[Refresh Token] 유효성 검사: {}", val);
        // JWT 토큰 재발급
        String id = jwtService.extractAccountIdFromToken(token);
        log.info("[Refresh Token] id: {}", id);
        // 전송
        LoginResponse jwt = jwtService.generateTokens(id);
        return ResponseEntity.ok(MainResponse.success(jwt));
    }
}
