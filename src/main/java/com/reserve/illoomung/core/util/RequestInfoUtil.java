package com.reserve.illoomung.core.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestInfoUtil {
    // IP 추출 (X-Forwarded-For 헤더 우선 적용)
    public static String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        } else {
            ip = ip.split(",")[0].trim();
        }
        return (ip != null && !ip.isEmpty()) ? ip : "unknown";
    }

    // User-Agent 추출
    public static String extractUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return (userAgent != null && !userAgent.isEmpty()) ? userAgent : "unknown";
    }
}
