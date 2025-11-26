package com.reserve.illoomung.application.es;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import com.reserve.illoomung.core.util.SecurityUtil;
import com.reserve.illoomung.domain.entity.StoreImage;
import com.reserve.illoomung.domain.entity.Stores;
import com.reserve.illoomung.domain.entity.es.StoreDocument;
import com.reserve.illoomung.domain.repository.es.StoreSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.client.elc.NativeQuery; // 변경됨
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders.simpleQueryString;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreSearchServiceImpl implements StoreSearchService {
    private final SecurityUtil securityUtil;
    private final ElasticsearchOperations operations;
    private final StoreSearchRepository storeSearchRepository;

    @Autowired
    @Lazy
    private StoreSearchService self;

    @Override
    public void syncStore(Stores store, List<String> categoryName, StoreImage storeImage, List<StoreDocument.OperatingHourDto> hour, List<String> amenities) {
        try {
            // 1. 검색을 위한 주소 통합 (Nori 분석용)
            String fullAddress = Stream.of(store.getAddrDepth1(), store.getAddrDepth2(), store.getAddrDepth3())
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining(" "));

            // 2. MySQL 데이터 -> ES 도큐먼트 변환
            // (검색에 필요한 필드만 쏙쏙 뽑아서 넣습니다)
            StoreDocument document = StoreDocument.builder()
                    .id(store.getStoreId())
                    .categories(categoryName.stream().toList())// MySQL PK와 동일하게 맞춤
                    .name(store.getStoreName())
                    .province(store.getAddrDepth1())
                    .city(store.getAddrDepth2())
                    .district(store.getAddrDepth3())
                    .fullAddress(fullAddress)    // "경기 김포시 구래동"
                    .addr(securityUtil.textDecrypt(store.getAddress()))
                    .imgUrl(storeImage.getImageUrl())
                    .operatingHours(hour)
                    .amenities(amenities)
                    .build();

            // 3. ES에 저장 (기존에 같은 ID가 있으면 덮어씌워짐 -> 수정 효과)
            storeSearchRepository.save(document);
            log.info("Elasticsearch 동기화 완료: Store ID {}", store.getStoreId());

        } catch (Exception e) {
            // ES 저장이 실패해도 메인 로직(MySQL 저장)은 롤백되지 않게 할지,
            // 같이 에러를 낼지 결정해야 합니다. 보통은 로그만 남기고 넘어갑니다.
            log.error("Elasticsearch 동기화 실패: Store ID {}", store.getStoreId(), e);
        }
    }

    // 2. 검색 (Redis 캐싱 적용)
    public List<StoreDocument> search(String query) {

        // 1. 순수 데이터 가져오기 (여기서 캐시가 동작함!)
        // this.searchFromIndex(query)가 아니라 self.searchFromIndex(query)여야 캐시가 먹힘
        List<StoreDocument> docs = self.searchFromIndex(query);
        // 2. 현재 시간 기준 영업 여부 계산 (Post-Processing)
        LocalDateTime now = LocalDateTime.now();
        int currentDay = now.getDayOfWeek().getValue(); // 0(일) ~ 6(토)
        LocalTime currentTime = now.toLocalTime();

        // 3. 결과 리스트를 순회하며 상태 업데이트
        // (주의: Redis에서 가져온 객체는 매번 새로운 인스턴스이므로 수정해도 원본 캐시에 영향 없음)
        for (StoreDocument doc : docs) {
            boolean isOpen = calculateIsOpen(doc.getOperatingHours(), currentDay, currentTime);
            doc.setOpenNow(isOpen);
        }

        return docs;
    }

    @Cacheable(cacheNames = "storeSearch", key = "#query", unless = "#result.isEmpty()")
    public List<StoreDocument> searchFromIndex(String query) {
        log.info("Elasticsearch 검색 수행 (No Highlight): {}", query);

        // 1. 쿼리 생성 (하이라이트 요청 부분 삭제됨)
        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .simpleQueryString(sq -> sq
                                .query(query) // "사우 카페"
                                // [핵심 1] 모든 필드 나열 (가중치 적용 가능)
                                .fields("fullAddress^2.0", "name^1.5", "categories^1.5", "district", "amenities^1.2")
                                // [핵심 2] 띄어쓰기로 구분된 단어들이 "모두" 있어야 함 (AND 조건)
                                .defaultOperator(Operator.And)
                        )
                )
                .build();

        // 2. 검색 실행
        SearchHits<StoreDocument> hits = operations.search(searchQuery, StoreDocument.class);

        // 3. 결과 반환 (복잡한 매핑 로직 없이 원본 데이터만 쏙 꺼냄)
        return hits.stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    // 영업 여부 계산 로직 (Private Helper)
    private boolean calculateIsOpen(List<StoreDocument.OperatingHourDto> hours, int currentDay, LocalTime currentTime) {
        // 1. 데이터가 없으면 영업 안 함
        if (hours == null || hours.isEmpty()) {
            return false;
        }

        return hours.stream()
                // 현재 요일(1:월 ~ 7:일)에 맞는 데이터 찾기
                .filter(h -> h.getDayOfWeek() == currentDay)
                .findFirst()
                .map(h -> {
                    // [방어 로직 1] 휴무일이면 false
                    if (h.isHoliday()) {
                        return false;
                    }

                    // [방어 로직 2 - NPE 해결 핵심] 시간이 null이면 계산 불가능하므로 false 처리
                    if (h.getOpenTime() == null || h.getCloseTime() == null) {
                        return false;
                    }

                    try {
                        // 시간 파싱 (HH:mm 형식이 맞다고 가정)
                        LocalTime open = LocalTime.parse(h.getOpenTime());
                        LocalTime close = LocalTime.parse(h.getCloseTime());

                        // [추가 로직] 새벽 영업(예: 18:00 ~ 02:00) 고려
                        if (close.isBefore(open)) {
                            // 자정을 넘긴 경우: (현재 >= 오픈) OR (현재 <= 마감)
                            return !currentTime.isBefore(open) || !currentTime.isAfter(close);
                        } else {
                            // 일반 영업(예: 09:00 ~ 18:00): 오픈 <= 현재 <= 마감
                            return !currentTime.isBefore(open) && !currentTime.isAfter(close);
                        }
                    } catch (Exception e) {
                        // 시간 형식이 이상하면 영업 안 함으로 처리 (로그 남기면 좋음)
                        return false;
                    }
                })
                // 데이터가 없으면 영업 안 함
                .orElse(false);
    }

    @Override
    public void deleteStore(Long storeId) {
        storeSearchRepository.deleteById(storeId);
        log.info("Elasticsearch 삭제 완료: Store ID {}", storeId);
    }
}