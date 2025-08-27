package com.reserve.illoomung.dto.request.auth;

import com.reserve.illoomung.core.domain.entity.enums.SocialProvider;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SocialRegisterLoginRequest {
    
    @NotNull(message = "소셜 제공자는 필수 입력 항목입니다.")
    @Schema(description = "소셜 제공사", example = "KAKAO, NAVER, GOOGLE")
    private SocialProvider socialProvider; // 소셜 제공자 (예: KAKAO, NAVER, GOOGLE 등)
}
