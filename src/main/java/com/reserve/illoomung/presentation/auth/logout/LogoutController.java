package com.reserve.illoomung.presentation.auth.logout;

import com.reserve.illoomung.application.auth.logout.LogoutService;
import com.reserve.illoomung.core.dto.MainResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/logout")
@RequiredArgsConstructor
public class LogoutController {

    private final LogoutService logoutService;

    @GetMapping
    public ResponseEntity<MainResponse<String>> Logout(@RequestHeader("Authorization") String accessToken,@RequestHeader("refresh-token") String refreshToken) {
        log.info("Access Token: {}", accessToken);
        log.info("Refresh Token: {}", refreshToken);
        logoutService.logout(accessToken, refreshToken);
        return ResponseEntity.ok(MainResponse.success());
    }
}
