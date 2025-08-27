package com.reserve.illoomung.presentation.auth.login;

import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.dto.request.auth.LocalRegisterLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "사용자 로컬 로그인",
            description = "사용자 로컬 로긍ㄴ 컨트롤러"
            // security = @SecurityRequirement(name = "BearerAuth")
    )
    @PostMapping("/local")
    public ResponseEntity<MainResponse<String>> loginController(@Valid @RequestBody LocalRegisterLoginRequest request) {
        return ResponseEntity.ok(MainResponse.success());
    }
}
