package com.reserve.illoomung.application.main;

import com.reserve.illoomung.core.util.SecurityUtil;
import com.reserve.illoomung.domain.entity.*;
import com.reserve.illoomung.domain.repository.*;
import com.reserve.illoomung.dto.main.MainPageResponse;
import com.reserve.illoomung.dto.main.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainServiceImpl implements MainService {

    private final Random random = new Random();
    private final SecurityUtil securityUtil;

    /**
     * TODO: 실무 환경에서는 변경
     * 중복 없는 랜덤 숫자 리스트를 생성합니다.
     *
     * @param count     생성할 숫자 개수
     * @param minBound  최소값 (포함)
     * @param maxBound  최대값 (미포함)
     * @return 중복 없는 랜덤 숫자 리스트
     * @throws IllegalArgumentException 범위를 초과하는 개수를 요청한 경우
     */
    public List<Integer> generateUniqueRandomNumbers(int count, int minBound, int maxBound) {

        long availableRange = (long) maxBound - minBound;

        // 💡 1. 예외 발생 로직
        // 생성 가능한 범위보다 많은 숫자를 요청했는지 확인합니다.
        if (availableRange < count) {
            throw new IllegalArgumentException(
                    "생성 가능한 범위(" + availableRange + "개)보다 많은 개수(" + count + "개)를 요청했습니다."
            );
        }

        // 💡 2. 랜덤 생성 로직 (Stream 사용)
        return random.ints(minBound, maxBound)
                .distinct()    // 중복 제거
                .limit(count)  // 요청한 개수만큼 자르기
                .boxed()
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

        // TODO: 실무 환경에서는 추천 시스템으로 변경
        List<Integer> test = generateUniqueRandomNumbers(7, 1, 50);
        List<Long> testList = test.stream().map(Integer::longValue).toList();
        List<Stores> storeList = storesRepository.findAllById(testList);
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
