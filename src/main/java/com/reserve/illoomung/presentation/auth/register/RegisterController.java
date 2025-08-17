package com.reserve.illoomung.presentation.auth.register;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reserve.illoomung.application.auth.register.RegisterService;
import com.reserve.illoomung.dto.request.auth.register.RegisterRequest;
import com.reserve.illoomung.core.domain.entity.enums.Role;
import com.reserve.illoomung.core.dto.MainResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final RegisterService registerService;

    @Operation(
        summary = "사용자 회원가입",
        description = "사용자 회원가입 컨트롤러"
        // security = @SecurityRequirement(name = "BearerAuth")
    )
    @PostMapping("/user")
    public ResponseEntity<MainResponse<String>> userRegister(@Valid @RequestBody RegisterRequest request) {
        registerService.register(
            request,
            Role.USER
        );
        return ResponseEntity.ok(MainResponse.success());
    }

}
