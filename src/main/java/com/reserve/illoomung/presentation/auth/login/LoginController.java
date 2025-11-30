package com.reserve.illoomung.presentation.auth.login;

import com.reserve.illoomung.application.auth.login.LoginService;
import com.reserve.illoomung.core.dto.LoginResponse;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.dto.request.auth.LocalLoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/local")
    public ResponseEntity<MainResponse<LoginResponse>> loginController(@Valid @RequestBody LocalLoginRequest request) {
        LoginResponse token = loginService.localLogin(request);
        log.info("--------AToken: {}", token.getAccessdToken());
        return ResponseEntity.ok(MainResponse.success(token));
    }
}
