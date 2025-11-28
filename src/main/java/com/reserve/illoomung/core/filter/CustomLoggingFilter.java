package com.reserve.illoomung.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class CustomLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. 요청 URL 확인
        String requestUri = request.getRequestURI();

        // 2. 현재 인증된 사용자 정보 가져오기 (SecurityContext)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            log.info("📢 [요청] URI: {} | 사용자: {} | 보유 권한: {}",
                    requestUri,
                    authentication.getName(),
                    authentication.getAuthorities());
        } else {
            log.info("👻 [요청] URI: {} | 사용자: 익명(토큰 없음/유효하지 않음)", requestUri);
        }

        filterChain.doFilter(request, response);
    }
}