package com.reserve.illoomung.application.business.storeInfo.product;

import com.reserve.illoomung.dto.business.StoreInfoResponse;

import java.util.List;

public interface StoreProductService {
    List<StoreInfoResponse.products> getStoreProductsAll(Long id);
    void saveStoreProduct(Long id, StoreInfoResponse.products product);
    void patchStoreProduct(Long storeId, Long productId, StoreInfoResponse.products product);
    void deleteStoreProduct(Long storeId, Long productId);
}
