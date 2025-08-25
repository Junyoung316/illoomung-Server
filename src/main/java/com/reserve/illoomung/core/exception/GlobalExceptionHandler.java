package com.reserve.illoomung.core.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.reserve.illoomung.core.dto.MainResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MainResponse<Void>> handleAllExceptions(Exception ex) {
        log.error("서버 오류 발생: {}", ex.getMessage(), ex.getCause());
        MainResponse<Void> errorResponse = MainResponse.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "서버 오류: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(NoAuthorizationHeaderException.class)
    public ResponseEntity<MainResponse<Void>> handleNoAuthorizationHeaderException(NoAuthorizationHeaderException ex) {
        log.error("헤더 오류 발생: {}", ex.getMessage(), ex.getCause());
        MainResponse<Void> errorResponse = MainResponse.error(
            HttpStatus.UNAUTHORIZED.value(),
            "헤더 오류: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    // NoAuthorizationHeaderException

    @ExceptionHandler(InvalidKakaoTokenException.class)
    public ResponseEntity<MainResponse<Void>> handleInvalidKakaoTokenException(InvalidKakaoTokenException ex) {
        log.error("잘못된 카카오 토큰: ", ex.getMessage(), ex.getClass());
        MainResponse<Void> errorResponse = MainResponse.error(
            HttpStatus.UNAUTHORIZED.value(),
            "잘못된 카카오 토큰: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(KakaoApiClientException.class)
    public ResponseEntity<MainResponse<Void>> handleKakaoApiClientException(KakaoApiClientException ex) {
        log.error("카카오 API 클라이언트 오류: ", ex.getMessage(), ex.getClass());
        MainResponse<Void> errorResponse = MainResponse.error(
            HttpStatus.BAD_REQUEST.value(),
            "카카오 API 클라이언트 오류: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(KakaoApiServerException.class)
    public ResponseEntity<MainResponse<Void>> handleKakaoApiServerException(KakaoApiServerException ex) {
        log.error("카카오 API 서버 오류: ", ex.getMessage(), ex.getClass());
        MainResponse<Void> errorResponse = MainResponse.error(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "카카오 API 서버 오류: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<MainResponse<Void>> handleDuplicateEmailException(DuplicateException ex) {
        log.error("중복 예외: ", ex.getMessage(), ex.getClass());
        MainResponse<Void> errorResponse = MainResponse.error(
            HttpStatus.CONFLICT.value(),
            "중복: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
