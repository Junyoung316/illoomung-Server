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
        log.info("상품 정보 조회 컨트롤러");
        return ResponseEntity.ok(MainResponse.success(storeProductService.getStoreProductsAll(id)));
    }

    // 가게 상품 조회 등록 수정 삭제
    @PostMapping("/product/{storeId}/save")
    public ResponseEntity<MainResponse<List<StoreInfoResponse.products>>> svaeStoreProducts(@PathVariable("storeId") Long id, @RequestBody StoreInfoResponse.products product) {
        storeProductService.saveStoreProduct(id, product);
        return ResponseEntity.ok(MainResponse.success());
    }
}
