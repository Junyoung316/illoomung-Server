package com.reserve.illoomung.application.auth.logout;

import com.reserve.illoomung.core.domain.repository.RefreshTokensRepository;
import com.reserve.illoomung.core.domain.repository.TokenBlacklistRepository;
import com.reserve.illoomung.core.util.jwt.application.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final JwtService jwtService;
    private final RefreshTokensRepository refreshTokensRepository; // 사용 중인 리프레시 토큰 테이블
    private final TokenBlacklistRepository tokenBlacklistRepository; // 로그아웃 처리되거나 비활성화 된 엑세스 및 리프레시 토큰

    // 토큰 추출 로직
    private String extractToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7); // "Bearer " 이후의 문자열만 추출
        }
        return null;
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        // TODO: 데이터베이스 토큰 만료 시간 통일 및 토큰 블랙리스트 테이블에 토큰 추가
        String Atoken = extractToken(accessToken);
        Long AtokenExp = null;
        if (Atoken != null) {
            AtokenExp = jwtService.extractFromToken(Atoken, "exp", Long.class);
            Date exp = new Date(AtokenExp * 1000);
            log.info("token exp: {}", AtokenExp);
            log.info("token exp: {}", exp);
        }

        String Rtoken = refreshToken;
        Long RtokenExp = null;
        if (Rtoken != null) {
            RtokenExp = jwtService.extractFromToken(Rtoken, "exp", Long.class);
            Date exp = new Date(RtokenExp * 1000);
            log.info("token exp: {}", RtokenExp);
            log.info("token exp: {}", exp);
        }
    }
}
