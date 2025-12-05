package com.reserve.illoomung.application.es;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reserve.illoomung.core.util.SecurityUtil;
import com.reserve.illoomung.domain.entity.StoreImage;
import com.reserve.illoomung.domain.entity.Stores;
import com.reserve.illoomung.domain.entity.es.StoreDocument;
import com.reserve.illoomung.domain.repository.es.StoreSearchRepository;
import com.reserve.illoomung.dto.business.StoreInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.elasticsearch.client.elc.NativeQuery; // 변경됨
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.ScriptType;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final ObjectMapper objectMapper;

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
        // 1. 순수 검색어 (분석기 적용)
        String rawQuery = query;

        // 2. 와일드카드 검색어 (포함 검색 강제 적용) -> "test" 검색 시 "*test*"로 변환
        String wildcardQuery = "*" + query + "*";

        log.info("ES 검색 수행: raw='{}', wildcard='{}'", rawQuery, wildcardQuery);

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .bool(b -> b
                                // [A] 가게 기본 정보 검색 (여긴 기존 필드명 유지)
                                .should(s -> s
                                        .simpleQueryString(sq -> sq
                                                .query(rawQuery + " | " + wildcardQuery)
                                                .fields("fullAddress^2.0", "name^1.5", "categories^1.5", "district", "amenities^1.2")
                                                .defaultOperator(Operator.Or)
                                        )
                                )
                                // [B] 상품 정보 검색 (Nested) -> ★ 여기 필드명을 수정해야 함! ★
                                .should(s -> s
                                        .nested(n -> n
                                                .path("products")
                                                .query(nq -> nq
                                                        .simpleQueryString(sq -> sq
                                                                .query(rawQuery + " | " + wildcardQuery)
                                                                // ▼▼▼ [핵심 수정] products.name -> products.productName ▼▼▼
                                                                .fields("products.productName", "products.productPrice")
                                                                .defaultOperator(Operator.Or)
                                                                .analyzeWildcard(true)
                                                        )
                                                )
                                        )
                                )
                        )
                )
                .build();

        SearchHits<StoreDocument> hits = operations.search(searchQuery, StoreDocument.class);
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

    // -------------------------------------------------------------
    // [추가] 상품(Product) 부분 업데이트 로직 (Painless Script 사용)
    // -------------------------------------------------------------

    /**
     * 상품 추가: 기존 가게 문서의 products 리스트에 새 상품을 추가합니다.
     */
    /**
     * 상품 추가
     */
    @Override
    @CacheEvict(cacheNames = "storeSearch", allEntries = true)
    public void addProductToStore(Long storeId, Long productId, StoreInfoResponse.products productDto) {
        // 1. DTO -> Map 변환
        Map<String, Object> originalMap = objectMapper.convertValue(productDto, new TypeReference<Map<String, Object>>() {});

        // 2. [중요] ES 필드명("productName" 등)에 맞춰서 새로운 Map 생성
        // (DTO 필드명이 name, price라고 가정하고, ES 필드명인 productName, productPrice로 매핑)
        Map<String, Object> esMap = new HashMap<>();

        esMap.put("productsId", productId); // [핵심] productsId 강제 주입

        // DTO의 필드명(getter)을 확인하여 매핑해주세요.
        // 예: dto.getName() -> "productName"
        esMap.put("productName", productDto.getProductName());
        esMap.put("productDescription", productDto.getProductDescription()); // description이 있다면
        esMap.put("productPrice", String.valueOf(productDto.getProductPrice())); // [핵심] String 타입으로 변환

        // 3. Painless 스크립트
        String script = "if (ctx._source.products == null) { ctx._source.products = new ArrayList(); } " +
                "ctx._source.products.add(params.product);";

        // 4. 파라미터 매핑
        Map<String, Object> params = new HashMap<>();
        params.put("product", esMap); // 변환된 esMap 사용

        // 5. 실행
        UpdateQuery updateQuery = UpdateQuery.builder(storeId.toString())
                .withScript(script)
                .withParams(params)
                .withScriptType(ScriptType.INLINE)
                .withLang("painless")
                .build();

        operations.update(updateQuery, IndexCoordinates.of("stores"));
        log.info("ES 상품 추가 완료: Store ID {}, Product ID {}", storeId, productId);
    }

    /**
     * 상품 삭제
     */
    @Override
    @CacheEvict(cacheNames = "storeSearch", allEntries = true)
    public void removeProductFromStore(Long storeId, Long productId) {
        // [수정] 스크립트 내 변수명을 id -> productsId 로 변경
        // [수정] 파라미터명을 params.productId -> params.productsId 로 변경
        String script = "if (ctx._source.products != null) { " +
                "  ctx._source.products.removeIf(p -> p.productsId == params.productsId); " +
                "}";

        Map<String, Object> params = new HashMap<>();
        params.put("productsId", productId); // 파라미터 Key 일치시키기

        UpdateQuery updateQuery = UpdateQuery.builder(storeId.toString())
                .withScript(script)
                .withParams(params)
                .withScriptType(ScriptType.INLINE)
                .withLang("painless")
                .build();

        operations.update(updateQuery, IndexCoordinates.of("stores"));
        log.info("ES 상품 삭제 완료: Store ID {}, Product ID {}", storeId, productId);
    }

    /**
     * 상품 수정
     */
    @Override
    @CacheEvict(cacheNames = "storeSearch", allEntries = true)
    public void updateProductInStore(Long storeId, Long productId, StoreInfoResponse.products productDto) {
        // 1. DTO -> ES 매핑용 Map으로 수동 변환 (필드명 불일치 해결)
        Map<String, Object> esMap = new HashMap<>();

        esMap.put("productsId", productId); // [중요] 수정 시에도 ID가 누락되지 않도록 주입
        esMap.put("productName", productDto.getProductName());
        esMap.put("productDescription", productDto.getProductDescription());
        esMap.put("productPrice", String.valueOf(productDto.getProductPrice())); // String 변환

        // 2. Painless 스크립트 수정 (id -> productsId)
        String script = "if (ctx._source.products != null) { " +
                "  for (int i = 0; i < ctx._source.products.size(); i++) { " +
                // [수정] p.id -> p.productsId, params.productId -> params.productsId
                "    if (ctx._source.products[i].productsId == params.productsId) { " +
                "      ctx._source.products[i] = params.newProduct; " +
                "    } " +
                "  } " +
                "}";

        Map<String, Object> params = new HashMap<>();
        params.put("productsId", productId);
        params.put("newProduct", esMap); // 변환된 Map 사용

        UpdateQuery updateQuery = UpdateQuery.builder(storeId.toString())
                .withScript(script)
                .withParams(params)
                .withScriptType(ScriptType.INLINE)
                .withLang("painless")
                .build();

        operations.update(updateQuery, IndexCoordinates.of("stores"));
        log.info("ES 상품 수정 완료: Store ID {}, Product ID {}", storeId, productId);
    }
}