package com.reserve.illoomung.application.business;

import com.reserve.illoomung.application.webClient.WebClientService;
import com.reserve.illoomung.dto.business.StoreCreateRequest;
import com.reserve.illoomung.dto.webClient.KakaoAddressResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final WebClientService webClientService;

    @Override
    public void createStore(StoreCreateRequest storeCreateRequest) {
//        webClientService.kakaoGetBCode(storeCreateRequest.getRoadAddress());
        KakaoAddressResponse addressInfo = webClientService.kakaoGetBCode(storeCreateRequest.getRoadAddress());
//        if (addressInfo == null || addressInfo.getDocuments() == null || addressInfo.getDocuments().isEmpty()) {
//            throw new IllegalArgumentException("유효하지 않은 API 응답입니다.");
//        }
        KakaoAddressResponse.Document address = addressInfo.getDocuments().get(0);

        log.info("====={}", addressInfo);

    }
}
