package com.reserve.illoomung.presentation.business;

import com.reserve.illoomung.application.business.BusinessService;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.dto.business.StoreCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
@Slf4j
public class BusinessController {

    private final BusinessService businessService;

    @PostMapping("/create")
    public ResponseEntity<MainResponse<String>> createStore(@RequestBody StoreCreateRequest storeCreateRequest) {
        log.info("Creating store {}", storeCreateRequest);
        businessService.createStore(storeCreateRequest);
        return ResponseEntity.ok(MainResponse.created());
    }

//    @PostMapping
//    public ResponseEntity<MainResponse<String>> update
}
