package com.reserve.illoomung.core.util.autocomplete.application;

import com.reserve.illoomung.domain.entity.Amenity;
import com.reserve.illoomung.domain.entity.StoreCategory;
import com.reserve.illoomung.domain.entity.Stores;
import com.reserve.illoomung.domain.repository.AmenityRepository;
import com.reserve.illoomung.domain.repository.StoreCategoryRepository;
import com.reserve.illoomung.domain.repository.StoreOfferingRepository;
import com.reserve.illoomung.domain.repository.StoresRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AutocompleteService {

    private final StringRedisTemplate redisTemplate;
    private final StoreCategoryRepository storeCategoryRepository; // 스토어 카테고리
    private final StoresRepository storesRepository; // 스토어 기본 정보 (업체명, 주소)
    private final AmenityRepository amenityRepository; // 편의시설

    private static final String KEYWORD_KEY = "autocomplete:keywords";
    private static final int MAX_SUGGESTIONS = 10;

    @Getter
    @RequiredArgsConstructor
    private static class KeywordScore {
        private final String keyword;
        private final long score;
    }

    // -----------------------------------------------

    // 초기 데이터 Redis에 로드
    @PostConstruct
    public void loadInitialData() {

        // 1. 카테고리 이름 수집
        List<String> categoryNames = storeCategoryRepository.findAll().stream()
                .map(StoreCategory::getCategoryName)
                .toList();

        // 2. 편의시설 이름 수집
        List<String> amenityNames = amenityRepository.findAll().stream()
                .map(Amenity::getAmenityName)
                .toList();

        // 3. 가게 이름 및 주소 수집 (버그 수정됨)
        List<Stores> storeList = storesRepository.findAll();
        List<String> storeNames = new ArrayList<>();
        List<String> storeAddrs = new ArrayList<>();

        for (Stores store : storeList) {
            storeNames.add(store.getStoreName());

            // [수정] String.join을 써서 깔끔하게 합치기 (버그 해결)
            String fullAddr = String.join(" ", store.getAddrDepth1(), store.getAddrDepth2(), store.getAddrDepth3());
            String addrDepth12 = String.join(" ", store.getAddrDepth1(), store.getAddrDepth2());
            String addrDepth23 = String.join(" ", store.getAddrDepth2(), store.getAddrDepth3());
            storeAddrs.add(fullAddr);
            storeAddrs.add(addrDepth12);
            storeAddrs.add(addrDepth23);
            storeAddrs.add(store.getAddrDepth1());
            storeAddrs.add(store.getAddrDepth2());
            storeAddrs.add(store.getAddrDepth3());

        }

        // 4. 리스트 합치기 (중복 제거를 위해 Set 사용 추천)
        Set<String> uniqueKeywords = new HashSet<>();
        uniqueKeywords.addAll(categoryNames);
        uniqueKeywords.addAll(amenityNames);
        uniqueKeywords.addAll(storeNames);
        uniqueKeywords.addAll(storeAddrs);

        // 최종 리스트 변환 (Redis 저장용)
        List<String> keywords = new ArrayList<>(uniqueKeywords);

        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        keywords.forEach(keyword -> {
            if (zSetOps.score(KEYWORD_KEY, keyword) == null) {
                zSetOps.add(KEYWORD_KEY, keyword.trim(), 0);
            }
        });

        System.out.println("Redis ZSET에 초기 데이터 로드 완료: " + keywords.size() + "개");
    }

    // -----------------------------------------------

    // 자동완성 검색 기능
    public List<String> getSuggestions(String query) {
        if (query == null || query.isEmpty()) return Collections.emptyList();

        String prefix = query.trim();
        RedisZSetCommands.Range range = RedisZSetCommands.Range.range()
                .gte(prefix)
                .lte(prefix + "\uffff");

        Set<String> members = redisTemplate.opsForZSet()
                .rangeByLex(KEYWORD_KEY, range); // Limit 없이 호출

        if (members == null || members.isEmpty()) return Collections.emptyList();

        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();
        List<KeywordScore> keywordScores = members.stream()
                .map(member -> new KeywordScore(member,
                        Optional.ofNullable(zSetOps.score(KEYWORD_KEY, member))
                                .orElse(0.0).longValue()))
                .sorted(Comparator.comparingLong(KeywordScore::getScore).reversed())
                .toList();

        return keywordScores.stream()
                .map(KeywordScore::getKeyword)
                .limit(MAX_SUGGESTIONS)
                .toList();
    }

    @Async
    public void addStoreKeyword(Stores store) {
        indexKeyword(store.getStoreName());
        String fullAddr = String.join(" ", store.getAddrDepth1(), store.getAddrDepth2(), store.getAddrDepth3());
        String addrDepth12 = String.join(" ", store.getAddrDepth1(), store.getAddrDepth2());
        String addrDepth23 = String.join(" ", store.getAddrDepth2(), store.getAddrDepth3());
        indexKeyword(fullAddr);
        indexKeyword(addrDepth12);
        indexKeyword(addrDepth23);
        indexKeyword(store.getAddrDepth1());
        indexKeyword(store.getAddrDepth2());
        indexKeyword(store.getAddrDepth3());
    }

    public void indexKeyword(String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            redisTemplate.opsForZSet().add(KEYWORD_KEY, keyword.trim(), 0);
        }
    }

    // TODO: 스토어 삭제에 추가
    public void removeKeyword(String keyword) { // 관리자용
        if (keyword != null && !keyword.isBlank()) {
            redisTemplate.opsForZSet().remove(KEYWORD_KEY, keyword.trim());
        }
    }

    // -----------------------------------------------

    // 검색 완료 시 점수 증가
    public void incrementKeywordScore(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            redisTemplate.opsForZSet().incrementScore(KEYWORD_KEY, keyword.trim(), 1);
        }
    }
}
