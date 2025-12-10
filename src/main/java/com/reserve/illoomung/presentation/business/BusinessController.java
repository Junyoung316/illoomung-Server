package com.reserve.illoomung.presentation.business;

import com.reserve.illoomung.application.business.BusinessService;
import com.reserve.illoomung.application.business.BusinessTestService;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.dto.business.OwnerGetMyStores;
import com.reserve.illoomung.dto.business.StoreCreateRequest;
import com.reserve.illoomung.dto.business.TestStoreCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
@Slf4j
public class BusinessController {

    private final BusinessTestService businessTestService;

    private final BusinessService businessService;

    @GetMapping("/my-store")
    public ResponseEntity<MainResponse<List<OwnerGetMyStores>>> getMyStore() {
        List<OwnerGetMyStores> store = businessService.getMyStore();
        log.info("getMyStore: {}", store.toString());
        return ResponseEntity.ok(MainResponse.success(store));
    }

    @PostMapping("/test/create")
    public ResponseEntity<MainResponse<String>> createTestStore(@RequestBody TestStoreCreateRequest storeCreateRequest) {
        log.info("Creating test store {}", storeCreateRequest);
        businessTestService.createTestStore(storeCreateRequest);
        return ResponseEntity.ok(MainResponse.created());
    }

    @PostMapping("/create")
    public ResponseEntity<MainResponse<String>> createStore(@RequestPart(value = "file", required = false) MultipartFile file, @RequestPart(value = "request") StoreCreateRequest storeCreateRequest) throws IOException {
        log.info("Creating store {}", storeCreateRequest);
        businessService.createStore(storeCreateRequest, file);
        return ResponseEntity.ok(MainResponse.created());
    }

    // TODO 전체 조회 및 수정
    @PostMapping("/{storeId}/patch")
    public ResponseEntity<MainResponse<String>> updateStore(@PathVariable("storeId") Long id, @RequestPart(value = "file", required = false) MultipartFile file, @RequestPart(value = "request") StoreCreateRequest storeCreateRequest) throws IOException {
        log.info("Updating store {}", storeCreateRequest);
        businessService.updateStore(id, storeCreateRequest, file);
        return ResponseEntity.ok(MainResponse.created());
    }

    @DeleteMapping("/{storeId}/delete")
    public ResponseEntity<MainResponse<String>> deleteStore(@PathVariable("storeId") Long id) {
        log.info("Deleting store {}", id);
        businessService.deleteStore(id);
        return ResponseEntity.ok(MainResponse.success());
    }
}
