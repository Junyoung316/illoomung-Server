package com.reserve.illoomung.dto.request.auth.register;

// import java.time.LocalDate;
// import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class RegisterRequest { // 클라이언트에서 서버로 전달하는 입력 데이터
    // @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email; // 사용자 이메일

    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하로 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함해야 합니다.")
    private String password; // 사용자 비밀번호

    @JsonProperty(defaultValue = "NONE")
    private String socialProvider = "NONE"; // 소셜 로그인 제공자
    private String socialToken; // 소셜 로그인 토큰

    // @NotBlank(message = "전화번호는 필수 입력값입니다.") // 회원가입 이후 인증 
    // @Pattern(regexp = "^01(?:0|1|[6-9])[-]?(?:\\d{3}|\\d{4})[-]?\\d{4}$", message = "올바른 전화번호 형식이 아닙니다.")
    // private String phone; // 사용자 전화번호

    // @NotBlank(message = "이름은 필수 입력값입니다.")
    // @Size(min = 2, max = 50, message = "이름은 2자 이상 입력해주세요.")
    // private String name; // 사용자 이름

    // @NotBlank(message = "닉네임은 필수 입력값입니다.")
    // @Size(min = 2, max = 50, message = "닉네임은 2자 이상 입력해주세요.")
    // private String nickName; // 사용자 이름

    // private String gender;

    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    // private LocalDate birth;

    // private String address; // 사용자 주소, 지오코딩을 통해 시/도, 시/군/구 정보로 변환 및 위도 및 경도 정보를 받아 처리에 사용

    // private String profileImageUrl; // 사용자 프로필 이미지 URL
}
