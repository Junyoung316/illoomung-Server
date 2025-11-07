package com.reserve.illoomung.application.business.storeInfo.product;

import com.reserve.illoomung.dto.business.StoreInfoResponse;

import java.util.List;

public interface StoreProductService {
    List<StoreInfoResponse.products> getStoreProductsAll(Long id);
}
