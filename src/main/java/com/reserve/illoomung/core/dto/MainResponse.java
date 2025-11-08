package com.reserve.illoomung.core.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MainResponse<T> {
    private int status; // 상태코드
    private String message; // 메시지
    private T data; // 데이터

    // 성공 응답 (데이터 있는 경우)
    public static <T> MainResponse<T> success(T data) {
        return MainResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .data(data)
                .build();
    }

    // 성공 응답 (데이터 없는 경우)
    public static <T> MainResponse<T> success() {
        return MainResponse.<T>builder()
                .status(HttpStatus.OK.value())
                .message("성공")
                .build(); // data 지정 안 함 → null로 처리
    }

    // 성공 응답 (데이터 있는 경우)
    public static <T> MainResponse<T> created(T data) {
        return MainResponse.<T>builder()
                .status(HttpStatus.CREATED.value())
                .message("성공")
                .data(data)
                .build();
    }

    // 생성 성공 응답 (데이터 없는 경우)
    public static <T> MainResponse<T> created() {
        return MainResponse.<T>builder()
                .status(HttpStatus.NO_CONTENT.value())
                .message("성공")
                .build(); // data 지정 안 함 → null로 처리
    }

    // 에러 응답
    public static <T> MainResponse<T> error(int status, String message) {
        return MainResponse.<T>builder()
                .status(status)
                .message(message)
                .build();
    }
}
