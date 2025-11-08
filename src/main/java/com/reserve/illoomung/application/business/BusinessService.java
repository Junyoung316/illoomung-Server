package com.reserve.illoomung.application.business;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.dto.business.StoreCreateRequest;

public interface BusinessService {
    void createStore(StoreCreateRequest storeCreateRequest);
}
