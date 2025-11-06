package com.reserve.illoomung.application.business.storeInfo;

import com.reserve.illoomung.dto.business.StoreInfoResponse;

public interface StoreInfoService {
    StoreInfoResponse findStoreInfo(Long storeId);
}
