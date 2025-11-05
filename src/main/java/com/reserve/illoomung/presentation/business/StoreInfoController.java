package com.reserve.illoomung.presentation.business;


import com.reserve.illoomung.application.business.StoreInfoService;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.dto.business.StoreInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
@RequiredArgsConstructor
@Slf4j
public class StoreInfoController {

    private final StoreInfoService storeInfoService;

    @PostMapping("/info/{storeId}")
    public ResponseEntity<MainResponse<StoreInfoResponse>> storeInfo(@PathVariable("storeId") Long id) {
        // id에 맞는 가게 상세 정보 조회
        StoreInfoResponse storeInfo = storeInfoService.findStoreInfo(id);
        return ResponseEntity.ok(MainResponse.success(storeInfo));
    }

}
