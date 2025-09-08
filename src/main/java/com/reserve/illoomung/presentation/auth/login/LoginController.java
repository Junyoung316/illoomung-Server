package com.reserve.illoomung.presentation.auth.login;

import com.reserve.illoomung.application.auth.login.LoginService;
import com.reserve.illoomung.core.dto.LoginResponse;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.dto.request.auth.LocalRegisterLoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/local")
    public ResponseEntity<MainResponse<LoginResponse>> loginController(@Valid @RequestBody LocalRegisterLoginRequest request) {
        LoginResponse token = loginService.localLogin(request);
        return ResponseEntity.ok(MainResponse.success(token));
    }
}
