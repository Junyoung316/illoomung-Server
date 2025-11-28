package com.reserve.illoomung.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ProfileRequest {

    private static final String PHONE_REGEX = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$";

    private String name; // 이름

    private String nickname; // 닉네임

    @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
    @Pattern(regexp = PHONE_REGEX, message = "휴대폰 번호 형식이 올바르지 않습니다. (예: 01012345678)")
    private String phone;
}
