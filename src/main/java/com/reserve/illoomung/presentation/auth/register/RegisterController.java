package com.reserve.illoomung.presentation.auth.register;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reserve.illoomung.application.auth.register.RegisterService;
import com.reserve.illoomung.dto.request.auth.register.LocalRegisterRequest;
import com.reserve.illoomung.dto.request.auth.register.SocialRegisterRequest;
import com.reserve.illoomung.core.dto.MainResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @Operation(
        summary = "사용자 로컬 회원가입",
        description = "사용자 로컬 회원가입 컨트롤러"
        // security = @SecurityRequirement(name = "BearerAuth")
    )
    @PostMapping("/local")
    public ResponseEntity<MainResponse<String>> userRegister(@Valid @RequestBody LocalRegisterRequest request) {
        registerService.localRegister(
            request
        );
        return ResponseEntity.ok(MainResponse.success());
    }

    @Operation(
        summary = "사용자 소셜 회원가입",
        description = "사용자 소셜 회원가입 컨트롤러",
        security = @SecurityRequirement(name = "BearerAuth")
    )
    @PostMapping("/social")
    public ResponseEntity<MainResponse<String>> socialRegister(
        @RequestHeader("Authorization") String authorizationHeader,
        @Valid @RequestBody SocialRegisterRequest request
        ) {
            registerService.socialRegister(
                request,
                authorizationHeader.replace("Bearer ", "")
            );
        return ResponseEntity.ok(MainResponse.success());
    }
}
