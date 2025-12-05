package com.reserve.illoomung.presentation.business;

import com.reserve.illoomung.application.business.storeInfo.product.StoreProductService;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.dto.business.StoreInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
@Slf4j
public class StoreProductController {

    private final StoreProductService storeProductService;

    @GetMapping("/product/{storeId}/all") //
    public ResponseEntity<MainResponse<List<StoreInfoResponse.products>>> getAllStoreProducts(@PathVariable("storeId") Long id) {
        log.info("{} 가게, 전체 상품 정보 조회", id);
        return ResponseEntity.ok(MainResponse.success(storeProductService.getStoreProductsAll(id)));
    }

    // 가게 상품 등록
    @PostMapping("/product/{storeId}/save")
    public ResponseEntity<MainResponse<List<StoreInfoResponse.products>>> svaeStoreProducts(@PathVariable("storeId") Long id, @RequestBody StoreInfoResponse.products product) {
        log.info("{} 가게, 상품 정보 등록", id);
        storeProductService.saveStoreProduct(id, product);
        return ResponseEntity.ok(MainResponse.success());
    }

    // 가게 상품 수정
    @PatchMapping("/product/{storeId}/{productId}/patch")
    public ResponseEntity<MainResponse<String>> patchStoreProducts(@PathVariable("storeId") Long storeId, @PathVariable("productId") Long productId, @RequestBody StoreInfoResponse.products product) {
        storeProductService.patchStoreProduct(storeId, productId, product);
        return ResponseEntity.ok(MainResponse.success());
    }

    // 가게 상품 삭제
    @DeleteMapping("/product/{storeId}/{productId}/delete")
    public ResponseEntity<MainResponse<String>> deleteStoreProduct(@PathVariable("storeId") Long storeId, @PathVariable("productId") Long productId) {
        storeProductService.deleteStoreProduct(storeId, productId);
        return ResponseEntity.ok(MainResponse.success());
    }
}
