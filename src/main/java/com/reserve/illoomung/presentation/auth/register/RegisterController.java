package com.reserve.illoomung.presentation.auth.register;

import com.reserve.illoomung.dto.request.auth.LocalRegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.reserve.illoomung.application.auth.register.RegisterService;
import com.reserve.illoomung.core.dto.MainResponse;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Slf4j
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
        return ResponseEntity.ok(MainResponse.created());
    }

//    @Operation(
//        summary = "사용자 소셜 회원가입",
//        description = "사용자 소셜 회원가입 컨트롤러",
//        security = @SecurityRequirement(name = "BearerAuth")
//    )
//    @PostMapping("/social")
//    public ResponseEntity<MainResponse<?>> socialRegister(
//        @RequestHeader("Authorization") String authorizationHeader,
//        @Valid @RequestBody SocialRegisterLoginRequest request
//        ) {
//        Optional<LoginResponse> loginResponseOpt =  registerService.socialRegister(
//                request,
//                authorizationHeader.replace("Bearer ", "")
//            );
//        // 로그인 토큰 반환
//        // 신규 가입 성공 메시지 반환
//        return loginResponseOpt.<ResponseEntity<MainResponse<?>>>map(loginResponse -> ResponseEntity.ok(MainResponse.success(loginResponse))).orElseGet(() -> ResponseEntity.ok(MainResponse.success()));
//    }

//    @CrossOrigin(origins = "http://127.0.0.1:3002") // CORS 설정
//    @PostMapping("/kakao")
//    public ResponseEntity<MainResponse<String>> kakaoRegisterLoginController(@RequestBody Map<String, Object> body) {
//        log.info("Request Body: {}", body);
//        String code = (String) body.get("code");
//        registerService.kakaoRegister(code);
//        return ResponseEntity.ok(MainResponse.success());
//    }
//
//    @PostMapping("/callback")
//    public ResponseEntity<MainResponse<String>> kakaoController(@RequestBody Map<String, Object> body) {
//        log.info("Request Body: {}", body);
//        return ResponseEntity.ok(MainResponse.success());
//    }
}
// "Authorization": "Bearer dsljhfiouasdhjkfhsdoih"
