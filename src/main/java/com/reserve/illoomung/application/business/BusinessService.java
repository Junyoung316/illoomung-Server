package com.reserve.illoomung.application.business;

import com.reserve.illoomung.dto.business.OwnerGetMyStores;
import com.reserve.illoomung.dto.business.StoreCreateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BusinessService {
    List<OwnerGetMyStores> getMyStore();
    void createStore(StoreCreateRequest storeCreateRequest, MultipartFile file) throws IOException;
    void updateStore(Long id, StoreCreateRequest storeCreateRequest, MultipartFile file) throws IOException;
    void deleteStore(Long id);
}
