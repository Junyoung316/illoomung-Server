package com.reserve.illoomung.presentation.business;

import com.reserve.illoomung.core.dto.MainResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
@Slf4j
public class ReserveInfoController {

    @GetMapping("/reserve/{storeId}")
    public ResponseEntity<MainResponse<String>> searchStoreReserveInfo(@PathVariable("storeId") Long id) {
        // id에 맞는 가게 예약 정보 조회
        return ResponseEntity.ok(MainResponse.success());
    }

    // 가게 예약 수정 삭제

}
