package com.reserve.illoomung.presentation.business;

import com.reserve.illoomung.application.business.BusinessService;
import com.reserve.illoomung.application.business.BusinessTestService;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.dto.business.StoreCreateRequest;
import com.reserve.illoomung.dto.business.TestStoreCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
@Slf4j
public class BusinessController {

    private final BusinessTestService businessTestService;

    private final BusinessService businessService;

    @PostMapping("/test/create")
    public ResponseEntity<MainResponse<String>> createTestStore(@RequestBody TestStoreCreateRequest storeCreateRequest) {
        log.info("Creating test store {}", storeCreateRequest);
        businessTestService.createTestStore(storeCreateRequest);
        return ResponseEntity.ok(MainResponse.created());
    }

    @PostMapping(
            name = "/create",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<MainResponse<String>> createStore(@RequestPart(value = "file", required = false) MultipartFile file, @RequestPart(value = "request") StoreCreateRequest storeCreateRequest) throws IOException {
        log.info("Creating store {}", storeCreateRequest);
        businessService.createStore(storeCreateRequest, file);
        return ResponseEntity.ok(MainResponse.created());
    }

    // 전체 조회 및 수정

//    @PostMapping
//    public ResponseEntity<MainResponse<String>> update
}
