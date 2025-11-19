package com.reserve.illoomung.application.business.storeInfo;

import com.reserve.illoomung.application.webClient.WebClientService;
import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.UserProfile;
import com.reserve.illoomung.core.domain.repository.UserProfileRepository;
import com.reserve.illoomung.core.util.SecurityUtil;
import com.reserve.illoomung.domain.entity.*;
import com.reserve.illoomung.domain.entity.enums.Status;
import com.reserve.illoomung.domain.repository.*;
import com.reserve.illoomung.dto.business.StoreInfoResponse;
import com.reserve.illoomung.dto.webClient.KakaoAddressResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreInfoServiceImpl implements StoreInfoService {

    private final WebClientService webClientService; // 외부 api 요청 서비스
    private final SecurityUtil securityUtil;

    private final UserProfileRepository userProfileRepository;

    private final StoresRepository storesRepository; // 가게 기본 정보 및 사업자 정보
    private final StoreImageRepository storeImageRepository; // 가게 이미지
    private final StoreAmenityMappingRepository storeAmenityMappingRepository; // 가게 편의시설
    private final StoreOperatingHoursRepository storeOperatingHoursRepository; // 영업시간
    private final StoreOfferingRepository storeOfferingRepository; // 상품정보

    private KakaoAddressResponse.Address getAddressAndBcodeFromApi(String addressBcode) {
        KakaoAddressResponse addressInfo = webClientService.kakaoGetBCode(addressBcode);
        if (addressInfo == null || addressInfo.getDocuments() == null || addressInfo.getDocuments().isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 API 응답입니다.");
        } // 데이터가 null이거나 빈 문자열일 때 예외 처리
        KakaoAddressResponse.Document address = addressInfo.getDocuments().getFirst();
        return address.getAddress();
    }

    private String convertDayOfWeekToString(Integer dayNum) {
        return switch (dayNum) {
            case 0 -> "일";
            case 1 -> "월";
            case 2 -> "화";
            case 3 -> "수";
            case 4 -> "목";
            case 5 -> "금";
            case 6 -> "토";
            case 7 -> "매일";
            default -> null;
        };
    }

    @Override
    public StoreInfoResponse findStoreInfo(Long storeId) {
        Stores store = storesRepository.findAllByStoreId(storeId).orElseThrow(() -> {
            log.error("존재하지 않는 가게");
            return new RuntimeException("존재하지 않는 가게");
        });
        Account owner = store.getOwner(); // 사업자 정보 조회
        String img = storeImageRepository.findByStoreStoreId(store.getStoreId())
                .map(StoreImage::getImageUrl)
                .orElse(null);

        List<StoreAmenityMapping> amenityMaps = storeAmenityMappingRepository
                .findByStoreStoreId(store.getStoreId());

        List<String> amenityNameList = amenityMaps.stream()
                .map(map -> map.getAmenity().getAmenityName()) // 👈 이름(String)만 추출
                .toList();

        String addr = securityUtil.textDecrypt(store.getAddress());
        String addrDetail =  securityUtil.textDecrypt(store.getAddressDetails());

        KakaoAddressResponse.Address address = getAddressAndBcodeFromApi(addr);
        String x = address.getX();
        String y = address.getY();

        List<StoreOperatingHours> storesOperatingHoursList = storeOperatingHoursRepository.findByStoreStoreIdOrderByDayOfWeekAsc(store.getStoreId());

        List<StoreInfoResponse.openCloseHours> storeOpenCloseList = storesOperatingHoursList.stream()
                .map(operatingHours -> {
                    boolean isOpen = operatingHours.getIsOpen();
                    String open;
                    String close = null;

                    if (isOpen) {
                        open = operatingHours.getOpenTime().toString();
                        close = operatingHours.getCloseTime().toString();
                    } else {
                        open = "정기 휴무";
                    }

                    String dayOfweek = convertDayOfWeekToString(operatingHours.getDayOfWeek());
                    return StoreInfoResponse.openCloseHours.builder()
                            .dayOfWeek(dayOfweek)
                            .isOpening(isOpen)
                            .openingHour(open)
                            .closingHour(close)
                            .build();
                }).toList();

        List<StoreOffering> storeProductsList = storeOfferingRepository.findByStoreStoreIdAndStatus(store.getStoreId(), Status.ACTIVE);

        List<StoreInfoResponse.products> storeProducts = storeProductsList.stream()
                .map(entity -> StoreInfoResponse.products.builder()
                        .productsId(entity.getOfferingId())
                        .productName(entity.getOfferingName())
                        .productDescription(entity.getDescription())
                        .productPrice(entity.getPrice().toString())
                        .build())
                .toList();

        UserProfile profile = userProfileRepository.findByAccount(owner)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        StoreInfoResponse.seller sellerInfo = StoreInfoResponse.seller.builder()
                .sellerId(owner.getAccountId())
                .sellerEmail(securityUtil.textDecrypt(owner.getEmail()))
                .sellerName(securityUtil.textDecrypt(profile.getName()))
                .build();

        return StoreInfoResponse.builder()
                .storeId(store.getStoreId())
                .imgUrl(img)
                .name(store.getStoreName())
                .description(store.getDescription())
                .amenities(amenityNameList)
                .addr(addr)
                .addrDetail(addrDetail)
                .x(x)
                .y(y)
                .openCloseHours(storeOpenCloseList)
                .products(storeProducts)
                .seller(sellerInfo)
                .build();
    }
}