package com.reserve.illoomung.application.business;

import com.reserve.illoomung.application.webClient.WebClientService;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.domain.repository.AmenityRepository;
import com.reserve.illoomung.domain.repository.StoreImageRepository;
import com.reserve.illoomung.domain.repository.StoresRepository;
import com.reserve.illoomung.dto.business.StoreInfoResponse;
import com.reserve.illoomung.dto.webClient.KakaoAddressResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreInfoServiceImpl implements StoreInfoService {

    private final WebClientService webClientService; // 외부 api 요청 서비스

    private final AccountRepository accountRepository; // 판매자 정보
    private final StoresRepository storesRepository; // 가게 기본 정보
    private final StoreImageRepository storeImageRepository; // 가게 이미지
    private final AmenityRepository amenityRepository; // 가게 편의시설

    private KakaoAddressResponse.Address getAddressAndBcodeFromApi(String addressBcode) {
        KakaoAddressResponse addressInfo = webClientService.kakaoGetBCode(addressBcode);
        if (addressInfo == null || addressInfo.getDocuments() == null || addressInfo.getDocuments().isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 API 응답입니다.");
        } // 데이터가 null이거나 빈 문자열일 때 예외 처리
        KakaoAddressResponse.Document address = addressInfo.getDocuments().getFirst();
//        String x = address.getRoadAddress().getX(); // 경도
//        String y = address.getRoadAddress().getY(); // 위도
//        log.info("====={}, {}", na, ad);
//        log.info("====={}, {}", y, x);
        return address.getAddress();
    }

    @Override
    public StoreInfoResponse findStoreInfo(Long storeId) {
        return null;
    }
}

/*
{
    storeId: id,
    imgUrl: https://---,
    name: name,
    description: null,
    amenities: {
        "주차",
        "예약",
        ...
    },
    addr: 풍무,
    addrDetail: 20호,
    x: 09.1412421,
    y: 12.3124242,
    day: 일 or 매일,
    openingHours: {
        "09:00", // 일
        "09:00",
        ...
    },
    closingHours: {
        "09:00", // 일
        "09:00",
        ...
    },
    products: {
        productsId:
        productName:
        productDescription:
        productPrice
    },
    seller: {
        sellerId:
        sellerName:
        sellerEmail
    }
}
 */