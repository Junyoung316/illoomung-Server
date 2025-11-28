package com.reserve.illoomung.application.business;

import com.reserve.illoomung.dto.business.StoreCreateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface BusinessService {
    void createStore(StoreCreateRequest storeCreateRequest, MultipartFile file) throws IOException;
    void updateStore(Long id, StoreCreateRequest storeCreateRequest, MultipartFile file) throws IOException;
}
