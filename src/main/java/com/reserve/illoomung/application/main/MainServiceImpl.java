package com.reserve.illoomung.application.main;

import com.reserve.illoomung.core.util.SecurityUtil;
import com.reserve.illoomung.domain.entity.*;
import com.reserve.illoomung.domain.entity.enums.StoreStatus;
import com.reserve.illoomung.domain.repository.*;
import com.reserve.illoomung.dto.main.MainPageResponse;
import com.reserve.illoomung.dto.main.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final Random random = new Random();
    private final SecurityUtil securityUtil;

    /**
     * Long 타입 리스트에서 지정된 개수(count)만큼 랜덤하게 값을 뽑습니다.
     *
     * @param numberList Long 숫자가 들어있는 원본 리스트 (예: 가게 ID 리스트)
     * @param count      뽑을 개수
     * @return 랜덤하게 뽑힌 Long 리스트
     */
    public List<Long> pickRandomNumbersFromList(List<Long> numberList, int count) {

        // 1. 예외 처리: 뽑을 개수가 리스트 전체 크기보다 클 수 없음
        if (numberList.size() < count) {
            throw new IllegalArgumentException(
                    "리스트의 크기(" + numberList.size() + "개)보다 많은 개수(" + count + "개)를 요청했습니다."
            );
        }

        // 2. 랜덤 추출 로직
        // random.ints는 '인덱스(0, 1, 2...)'를 생성하므로 그대로 int를 사용합니다.
        return random.ints(0, numberList.size()) // 0 ~ (리스트크기-1) 사이의 인덱스 생성
                .distinct()                      // 중복 인덱스 제거
                .limit(count)                    // 개수 제한
                .mapToObj(numberList::get)       // 인덱스(int)로 리스트의 값(Long)을 가져옴
                .collect(Collectors.toList());
    }

    private final StoresRepository storesRepository; // 스토어 기본 정보
    private final StoreImageRepository storeImageRepository; // 스토어 이미지
    private final StoreOperatingHoursRepository  storeOperatingHoursRepository; // 스토어 영업시간
    private final StoreAmenityMappingRepository storeAmenityMappingRepository; // 스토어 편의 시설 매핑
    private final AmenityRepository amenityRepository; // 편의 시설 목록

    @Override
    @Transactional(readOnly = true)
    public List<MainPageResponse> mainInit() {

        List<Stores> activeStores = storesRepository.findByStatus(StoreStatus.ACTIVE).orElseThrow(() -> new RuntimeException("데이터를 찾을 수 없습니다."));
        List<Long> storeIds = new ArrayList<>();
        for  (Stores store : activeStores) {
            storeIds.add(store.getStoreId());
        }
        // TODO: 실무 환경에서는 추천 시스템으로 변경
        List<Long> test = pickRandomNumbersFromList(storeIds, 7);
//        List<Long> testList = test.stream().map(Integer::longValue).toList();
        List<Stores> storeList = storesRepository.findAllById(test);
        List<Long> foundId = storeList.stream().map(Stores::getStoreId).toList();
        log.info("list: {}", foundId);

        // 이미지 url 파싱
        Map<Long, String> storesImage = storeImageRepository.findByStoreStoreIdIn(foundId) // 👈 2번 문제도 수정
                .stream()
                .collect(
                        Collectors.toMap(
                                storeImage -> storeImage.getStore().getStoreId(), // Key
                                StoreImage::getImageUrl,                         // Value
                                (existingImageUrl, newImageUrl) -> existingImageUrl // ⬅️ [해결] 중복 시 첫 번째 값 사용
                        )
                );

        // 💡 [핵심 1] DB 규칙에 맞게 "오늘 요일" 계산 (일=0, 월=1... 토=6)
        int dbToday = LocalDate.now().getDayOfWeek().getValue() % 7;

        // 💡 [핵심 2] DB에서 '매일'을 의미하는 숫자
        final int EVERYDAY_CODE = 7;

        // 💡 [핵심 3] 오늘 요일과 매일 요일 코드를 리스트로 준비
        List<Integer> daysToSearch = List.of(dbToday, EVERYDAY_CODE);

        // 💡 [핵심 4] 1번 쿼리: '오늘(dbToday)' 또는 '매일(7)' 레코드를 모두 가져옴
        List<StoreOperatingHours> openingHoursList = storeOperatingHoursRepository
                .findByStoreStoreIdInAndDayOfWeekIn(foundId, daysToSearch);

        // 💡 [핵심 5] List -> Map으로 변환 (우선순위 적용)
        // Key: Store ID, Value: 최종 OpeningHours 객체
        Map<Long, StoreOperatingHours> hoursMap = openingHoursList.stream()
                .collect(Collectors.toMap(
                        oh -> oh.getStore().getStoreId(), // Key: 업체 ID
                        oh -> oh,                    // Value: OpeningHours 객체 자체
                        (oh1, oh2) -> {
                            // 💡 [핵심 6] 충돌 시 우선순위 처리
                            // oh1과 oh2가 같은 Store ID로 충돌함 (하나는 '오늘', 하나는 '매일')
                            // '매일(7)'이 아닌 레코드 (즉, '오늘' 레코드)가 우선순위를 가짐
                            if (oh1.getDayOfWeek() != EVERYDAY_CODE) {
                                return oh1; // oh1이 '오늘' 레코드
                            } else {
                                return oh2; // oh2가 '오늘' 레코드 (또는 둘 다 '매일'이라 상관없음)
                            }
                        }
                ));

        /// 💡 [핵심 1] 쿼리 (단 1번): ID 리스트로 모든 편의시설 '이름'까지 한 번에 조회
        List<StoreAmenityMapping> allAmenityMaps = storeAmenityMappingRepository
                .findAmenitiesByStoreStoreIdsIn(foundId);

        // 💡 [핵심 2] 그룹핑 (메모리):
        Map<Long, List<String>> amenityMap = allAmenityMaps.stream()
                .collect(Collectors.groupingBy(
                        // Key: Store ID (StoreAmenityMap -> Store -> storeId)
                        map -> map.getStore().getStoreId(),

                        // Value: Amenity 이름 (StoreAmenityMap -> Amenity -> name)
                        Collectors.mapping(
                                map -> map.getAmenity().getAmenityName(), // 👈 이름(String)만 추출
                                Collectors.toList()
                        )
                ));

        log.info("storeImage: {}", storesImage);
        log.info("openingHoursList: {}", hoursMap);
        log.info("amenityMap: {}", amenityMap);

        return storeList.stream()
                .map(entity -> {
                    StoreOperatingHours oh = hoursMap.get(entity.getStoreId());
                    boolean isOpen = (oh != null &&
                            oh.getIsOpen() != null &&
                            oh.getIsOpen());
                    String status = isOpen ? "영업 중" : "영업 종료";

                    List<String> amenityNameList = amenityMap.getOrDefault(entity.getStoreId(), Collections.emptyList());

                    return MainPageResponse.builder()
                            .StoreId(entity.getStoreId())
                            .storeName(entity.getStoreName())
                            .imgUrl(entity.getImages().getFirst().getImageUrl())
                            .addrDepth(entity.getAddrDepth3())
                            .isOpen(status)
                            .amenities(amenityNameList)
                            .build();
                }).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<SearchResponse> searchItem(String item) {
        log.info("searchItem: {}", item);

        List<Stores> stores = storesRepository.searchStores(item);
        List<Long> foundId = stores.stream().map(Stores::getStoreId).toList();

        Map<Long, String> storesImage = storeImageRepository.findByStoreStoreIdIn(foundId) // 👈 2번 문제도 수정
                .stream()
                .collect(
                        Collectors.toMap(
                                storeImage -> storeImage.getStore().getStoreId(), // Key
                                StoreImage::getImageUrl,                         // Value
                                (existingImageUrl, newImageUrl) -> existingImageUrl // ⬅️ [해결] 중복 시 첫 번째 값 사용
                        )
                );

        // 💡 [핵심 1] DB 규칙에 맞게 "오늘 요일" 계산 (일=0, 월=1... 토=6)
        int dbToday = LocalDate.now().getDayOfWeek().getValue() % 7;

        // 💡 [핵심 2] DB에서 '매일'을 의미하는 숫자
        final int EVERYDAY_CODE = 7;

        // 💡 [핵심 3] 오늘 요일과 매일 요일 코드를 리스트로 준비
        List<Integer> daysToSearch = List.of(dbToday, EVERYDAY_CODE);

        // 💡 [핵심 4] 1번 쿼리: '오늘(dbToday)' 또는 '매일(7)' 레코드를 모두 가져옴
        List<StoreOperatingHours> openingHoursList = storeOperatingHoursRepository
                .findByStoreStoreIdInAndDayOfWeekIn(foundId, daysToSearch);

        // 💡 [핵심 5] List -> Map으로 변환 (우선순위 적용)
        // Key: Store ID, Value: 최종 OpeningHours 객체
        Map<Long, StoreOperatingHours> hoursMap = openingHoursList.stream()
                .collect(Collectors.toMap(
                        oh -> oh.getStore().getStoreId(), // Key: 업체 ID
                        oh -> oh,                    // Value: OpeningHours 객체 자체
                        (oh1, oh2) -> {
                            // 💡 [핵심 6] 충돌 시 우선순위 처리
                            // oh1과 oh2가 같은 Store ID로 충돌함 (하나는 '오늘', 하나는 '매일')
                            // '매일(7)'이 아닌 레코드 (즉, '오늘' 레코드)가 우선순위를 가짐
                            if (oh1.getDayOfWeek() != EVERYDAY_CODE) {
                                return oh1; // oh1이 '오늘' 레코드
                            } else {
                                return oh2; // oh2가 '오늘' 레코드 (또는 둘 다 '매일'이라 상관없음)
                            }
                        }
                ));
        
        List<StoreAmenityMapping> allAmenityMaps = storeAmenityMappingRepository
                .findAmenitiesByStoreStoreIdsIn(foundId);

        Map<Long, List<String>> amenityMap = allAmenityMaps.stream()
                .collect(Collectors.groupingBy(
                        // Key: Store ID (StoreAmenityMap -> Store -> storeId)
                        map -> map.getStore().getStoreId(),

                        // Value: Amenity 이름 (StoreAmenityMap -> Amenity -> name)
                        Collectors.mapping(
                                map -> map.getAmenity().getAmenityName(), // 👈 이름(String)만 추출
                                Collectors.toList()
                        )
                ));

        return stores.stream()
                .map(entity -> {
                    log.info("addr: {}", entity.getAddress());
                    String addrDecrypt =  securityUtil.textDecrypt(entity.getAddress());
                    log.info("addrDecrypt: {}", addrDecrypt);
                    StoreOperatingHours oh = hoursMap.get(entity.getStoreId());
                    boolean isOpen = (oh != null &&
                            oh.getIsOpen() != null &&
                            oh.getIsOpen());
                    String status = isOpen ? "영업 중" : "영업 종료";

                    List<String> amenityNameList = amenityMap.getOrDefault(entity.getStoreId(), Collections.emptyList());

                    return SearchResponse.builder()
                            .StoreId(entity.getStoreId())
                            .storeName(entity.getStoreName())
                            .imgUrl(entity.getImages().getFirst().getImageUrl())
                            .addr(addrDecrypt)
                            .isOpen(status)
                            .amenities(amenityNameList)
                            .build();
                }).collect(Collectors.toList());
    }

}
