package com.reserve.illoomung.application.business;

import com.reserve.illoomung.dto.business.StoreCreateRequest;

public interface BusinessService {
    void createTestStore(StoreCreateRequest storeCreateRequest);
    void createStore(StoreCreateRequest storeCreateRequest);
}
