package com.reserve.illoomung.application.business;

import com.reserve.illoomung.application.webClient.WebClientService;
import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.core.dto.CryptoResult;
import com.reserve.illoomung.core.util.SecurityUtil;
import org.springframework.security.core.Authentication;
import com.reserve.illoomung.domain.entity.*;
import com.reserve.illoomung.domain.entity.enums.ImageType;
import com.reserve.illoomung.domain.repository.*;
import com.reserve.illoomung.dto.business.OperatingInfo;
import com.reserve.illoomung.dto.business.StoreCreateRequest;
import com.reserve.illoomung.dto.webClient.KakaoAddressResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BusinessServiceImpl implements BusinessService {

    private final WebClientService webClientService; // 외부 api 요청 서비스
    private final SecurityUtil securityUtil; // 암호화 모듈

    private final AccountRepository accountRepository; // 사용자 정보

    private final StoresRepository storesRepository; // 스토어 기본 정보

    private final StoreCategoryMappingRepository storeCategoryMappingRepository; // 스토어 카테고리 매핑
    private final StoreCategoryRepository storeCategoryRepository; // 스토어 카테고리 리스트

    private final StoreImageRepository storeImageRepository; // 스토어 이미지

    private final StoreOperatingHoursRepository storeOperatingHoursRepository; // 스토어 오픈시간

    private final StoreAmenityMappingRepository storeAmenityMappingRepository; // 편의시설 매핑
    private final AmenityRepository amenityRepository; // 편의시설 리스트

    private boolean checkNameAndAddressDuplicate(String name, String address, String addressDetails) {
        return storesRepository.existsByStoreNameAndAddressFullHashAndAddressDetailsHash(name, address, addressDetails);
    }

    private String getAddressAndBcodeFromApi(String addressBcode) {
        KakaoAddressResponse addressInfo = webClientService.kakaoGetBCode(addressBcode);
        if (addressInfo == null || addressInfo.getDocuments() == null || addressInfo.getDocuments().isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 API 응답입니다.");
        } // 데이터가 null이거나 빈 문자열일 때 예외 처리
        KakaoAddressResponse.Document address = addressInfo.getDocuments().getFirst();
//        String na = address.getRoadAddress().getAddressName(); // 지도
//        String bcode = address.getAddress().getBCode(); // 지역 코드
//        String x = address.getRoadAddress().getX(); // 경도
//        String y = address.getRoadAddress().getY(); // 위도
//        log.info("====={}, {}", na, ad);
//        log.info("====={}, {}", y, x);
        return address.getAddress().getBCode();
    }

    private void saveStore(Account account, StoreCreateRequest storeCreateRequest, CryptoResult phoneCrypto, CryptoResult addressCrypto, CryptoResult addressDetailsCrypto, String bCode) {
        Stores store = Stores.builder()
                .owner(account)
                .storeName(storeCreateRequest.getStoreName())
                .description(storeCreateRequest.getDescription())
                .phone(phoneCrypto.encryptedData())
                .address(addressCrypto.encryptedData())
                .addressFullHash(addressCrypto.hashedData())
                .addressDetails(addressDetailsCrypto.encryptedData())
                .addressDetailsHash(addressDetailsCrypto.hashedData())
                .bcode(bCode)
                .websiteUrl(storeCreateRequest.getHomepageUrl())
                .instagramUrl(storeCreateRequest.getInstagramUrl())
                .build();
        Stores saveStore = storesRepository.save(store);

        if (storeCreateRequest.getMainImageUrl() != null && !storeCreateRequest.getMainImageUrl().isEmpty()) {
            StoreImage storeImage = StoreImage.builder()
                    .store(saveStore)
                    .imageUrl(storeCreateRequest.getMainImageUrl())
                    .imageType(ImageType.MAIN) // TODO: 실제 환경에서 변경
                    .altText("가게 사진")
                    .build();
            storeImageRepository.save(storeImage);
        }

        Map<String, OperatingInfo> openingHoursMap = storeCreateRequest.getOpeningHours();
        if (openingHoursMap != null && !openingHoursMap.isEmpty()) {
            List<StoreOperatingHours> hoursList = convertToOperationHours(saveStore, openingHoursMap);
            storeOperatingHoursRepository.saveAll(hoursList);
        }

        List<String> amenityNames = storeCreateRequest.getAmenities();
        if (amenityNames != null && !amenityNames.isEmpty()) {
            // 2-1. 이름 목록으로 Amenity 엔티티 목록을 한번에 조회
            List<Amenity> foundAmenities = amenityRepository.findByAmenityNameIn(amenityNames);

            // 2-2. (선택적) 요청된 편의시설이 DB에 모두 존재하는지 검증
            if (foundAmenities.size() != amenityNames.size()) {
                throw new IllegalArgumentException("존재하지 않는 편의시설 이름이 포함되어 있습니다.");
            }

            // 2-3. StoreAmenityMapping 엔티티 목록 생성
            List<StoreAmenityMapping> mappings = foundAmenities.stream()
                    .map(amenity -> new StoreAmenityMapping(saveStore, amenity))
                    .collect(Collectors.toList());

            // 2-4. 생성된 매핑 정보들을 한번에 저장
            storeAmenityMappingRepository.saveAll(mappings);
        }

        List<String> categoryName = List.of(storeCreateRequest.getCategory());

        // 2-1. 이름 목록으로 Amenity 엔티티 목록을 한번에 조회
        List<StoreCategory> foundCategory = storeCategoryRepository.findByCategoryNameIn(categoryName);

        // 2-2. (선택적) 요청된 편의시설이 DB에 모두 존재하는지 검증
        if (foundCategory.size() != categoryName.size()) {
            throw new IllegalArgumentException("존재하지 않는 가게 카테고리 이름이 포함되어 있습니다.");
        }

        // 2-3. StoreAmenityMapping 엔티티 목록 생성
        List<StoreCategoryMapping> mappings = foundCategory.stream()
                .map(category -> new StoreCategoryMapping(saveStore, category))
                .collect(Collectors.toList());

        // 2-4. 생성된 매핑 정보들을 한번에 저장
        storeCategoryMappingRepository.saveAll(mappings);
    }

    @Override
    @Transactional
    public void createStore(StoreCreateRequest storeCreateRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("authenticated: {}", authentication);
        if(authentication != null && authentication.isAuthenticated()) {
            String userId = authentication.getName();  // 사용자 식별자(ID) 조회
            log.info("authenticated id: {}", userId);

            Account account = accountRepository.findByAccountId(Long.valueOf(userId))
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 사용자입니다."));
            log.info("authenticated account: {}", account);
            String bCode = getAddressAndBcodeFromApi(storeCreateRequest.getAddress());

            CryptoResult phoneCrypto = securityUtil.cryptoResult(storeCreateRequest.getPhoneNumber());
            CryptoResult addressCrypto = securityUtil.cryptoResult(storeCreateRequest.getAddress());
            CryptoResult addressDetailsCrypto = securityUtil.cryptoResult(storeCreateRequest.getAddressDetails());

            if (checkNameAndAddressDuplicate(storeCreateRequest.getStoreName(), addressCrypto.hashedData(), addressDetailsCrypto.hashedData())) {
                throw new IllegalStateException("이미 동일한 이름과 주소로 등록된 사업장이 존재합니다.");
            }

            saveStore(account, storeCreateRequest, phoneCrypto, addressCrypto, addressDetailsCrypto, bCode);
        }
    }

    private List<StoreOperatingHours> convertToOperationHours(Stores store, Map<String, OperatingInfo> openingHoursMap) {
        List<StoreOperatingHours> hoursList = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Map.Entry<String, OperatingInfo> entry : openingHoursMap.entrySet()) {
            String dayKey = entry.getKey();
            OperatingInfo info = entry.getValue();

            if ("매일".equals(dayKey)) {
                for (int i = 0; i <= 6; i++) {
                    hoursList.add(parseDayInfo(store, i, info, timeFormatter));
                }
            } else {
                int dayOfWeek = convertDayKeyToDayOfWeek(dayKey);
                hoursList.add(parseDayInfo(store, dayOfWeek, info, timeFormatter));
            }
        }
        return hoursList;
    }

    private StoreOperatingHours parseDayInfo(Stores store, int dayOfWeek, OperatingInfo info, DateTimeFormatter formatter) {
        StoreOperatingHours operationHours = new StoreOperatingHours();
        operationHours.setStore(store);
        operationHours.setDayOfWeek(dayOfWeek);

        String time = info.getTime();
        if (time == null || time.contains("휴무")) {
            operationHours.setIsOpen(false);
        } else {
            operationHours.setIsOpen(true);
            String[] times = time.split(" - ");
            if (times.length == 2) {
                operationHours.setOpenTime(LocalTime.parse(times[0], formatter));
                operationHours.setCloseTime(LocalTime.parse(times[1], formatter));
            }
        }

        String breakTime = info.getBreakTime();
        if (breakTime != null && !breakTime.trim().isEmpty()) {
            String[] times = breakTime.split(" - ");
            if (times.length == 2) {
                try {
                    operationHours.setBreakStartTime(LocalTime.parse(times[0].trim(), formatter));
                    operationHours.setBreakEndTime(LocalTime.parse(times[1].trim(), formatter));
                } catch (Exception e) {
                    // 브레이크 타임 파싱 실패 시 로그만 남기고 넘어갈 수 있습니다.
                }
            }
        }

        return operationHours;
    }

    private int convertDayKeyToDayOfWeek(String dayKey) {
        // 잘못된 요일 키가 들어올 경우 예외 발생
        return switch (dayKey) {
            case "일", "일요일" -> 0;
            case "월", "월요일" -> 1;
            case "화", "화요일" -> 2;
            case "수", "수요일" -> 3;
            case "목", "목요일" -> 4;
            case "금", "금요일" -> 5;
            case "토", "토요일" -> 6;
            case "매일" -> 7;
            default -> throw new IllegalArgumentException("Invalid day key: " + dayKey);
        };
    }
}
