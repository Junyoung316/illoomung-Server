package com.reserve.illoomung.core.util.autocomplete.application;

import com.reserve.illoomung.domain.entity.Amenity;
import com.reserve.illoomung.domain.entity.StoreCategory;
import com.reserve.illoomung.domain.entity.StoreOffering;
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
    private final StoreOfferingRepository storeOfferingRepository;

    @Getter
    @RequiredArgsConstructor
    private static class KeywordScore {
        private final String keyword;
        private final long score;
    }

    // -----------------------------------------------

    // 초기 데이터 Redis에 로드
    // ★ 키를 2개로 분리합니다 ★
    private static final String SEARCH_KEY = "autocomplete:search"; // 검색용 (Score 무조건 0)
    private static final String RANK_KEY = "autocomplete:rank";     // 랭킹용 (인기도 점수)
    private static final int MAX_SUGGESTIONS = 10; // 최대 추천 개수

    @PostConstruct
    public void loadInitialData() {
        // 기존 데이터 삭제 (중복/꼬임 방지)
        redisTemplate.delete(SEARCH_KEY);
        // 랭킹 데이터는 지우면 안 되므로 주석 처리 (원하면 지우세요)
        // redisTemplate.delete(RANK_KEY);

        // 1. 카테고리 이름 수집
        List<String> categoryNames = storeCategoryRepository.findAll().stream()
                .map(StoreCategory::getCategoryName)
                .toList();

        // 2. 편의시설 이름 수집
        List<String> amenityNames = amenityRepository.findAll().stream()
                .map(Amenity::getAmenityName)
                .toList();

        // 3. 가게 이름 및 주소 수집
        List<Stores> storeList = storesRepository.findAll();
        Set<String> uniqueKeywords = new HashSet<>(); // Set으로 중복 제거

        List<String> storeOfferingList = storeOfferingRepository.findAll().stream()
                .map(StoreOffering::getOfferingName)
                .toList();

        for (Stores store : storeList) {
            uniqueKeywords.add(store.getStoreName()); // 가게 이름

            // 주소 조합
            uniqueKeywords.add(String.join(" ", store.getAddrDepth1(), store.getAddrDepth2(), store.getAddrDepth3()));
            uniqueKeywords.add(String.join(" ", store.getAddrDepth1(), store.getAddrDepth2()));
            uniqueKeywords.add(String.join(" ", store.getAddrDepth2(), store.getAddrDepth3()));
            uniqueKeywords.add(store.getAddrDepth1());
            uniqueKeywords.add(store.getAddrDepth2());
            uniqueKeywords.add(store.getAddrDepth3());
        }

        // 4. 카테고리와 편의시설도 키워드에 추가
        uniqueKeywords.addAll(categoryNames);
        uniqueKeywords.addAll(amenityNames);
        uniqueKeywords.addAll(storeOfferingList);

        // Redis 저장 (Batch 처리 권장하지만, 일단 loop로 구현)
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        log.info("Redis 초기 데이터 로드 시작: {}개", uniqueKeywords.size());

        for (String keyword : uniqueKeywords) {
            if (keyword == null || keyword.isBlank()) continue;
            String trimmed = keyword.trim();

            // [핵심 1] 검색용 Key에 저장 (Score는 무조건 0)
            zSetOps.add(SEARCH_KEY, trimmed, 0);

            // [핵심 2] 랭킹용 Key에도 없으면 0점으로 초기화 (선택사항)
            // (랭킹 키에 이미 점수가 있다면 건드리지 않음)
            zSetOps.addIfAbsent(RANK_KEY, trimmed, 0);
        }

        log.info("Redis 초기 데이터 로드 완료");
    }

    // -----------------------------------------------

    // 자동완성 검색 기능 (수정됨)
    public List<String> getSuggestions(String query) {
        if (query == null || query.isEmpty()) return Collections.emptyList();

        String prefix = query.trim();

        // 1. [검색용 Key]에서 rangeByLex로 검색 (Score가 모두 0이라 잘 됨)
        RedisZSetCommands.Range range = RedisZSetCommands.Range.range()
                .gte(prefix)
                .lte(prefix + "\uffff");

        Set<String> members = redisTemplate.opsForZSet()
                .rangeByLex(SEARCH_KEY, range);

        if (members == null || members.isEmpty()) return Collections.emptyList();

        // 2. [랭킹용 Key]에서 점수 가져와서 정렬
        ZSetOperations<String, String> zSetOps = redisTemplate.opsForZSet();

        List<KeywordScore> keywordScores = members.stream()
                .map(member -> {
                    // 랭킹 키에서 점수 조회 (없으면 0.0)
                    Double score = zSetOps.score(RANK_KEY, member);
                    return new KeywordScore(member, score != null ? score.longValue() : 0L);
                })
                // 점수 높은 순 -> 사전순 정렬
                .sorted(Comparator.comparingLong(KeywordScore::getScore).reversed()
                        .thenComparing(KeywordScore::getKeyword))
                .limit(MAX_SUGGESTIONS)
                .toList();

        return keywordScores.stream()
                .map(KeywordScore::getKeyword)
                .toList();
    }

    @Async
    public void addStoreKeyword(Stores store) {
        // 가게 이름
        indexKeyword(store.getStoreName());

        // 주소 조합들
        indexKeyword(String.join(" ", store.getAddrDepth1(), store.getAddrDepth2(), store.getAddrDepth3()));
        indexKeyword(String.join(" ", store.getAddrDepth1(), store.getAddrDepth2()));
        indexKeyword(String.join(" ", store.getAddrDepth2(), store.getAddrDepth3()));
        indexKeyword(store.getAddrDepth1());
        indexKeyword(store.getAddrDepth2());
        indexKeyword(store.getAddrDepth3());

        // ★ 중요: 가게가 추가될 때, 그 가게의 카테고리나 편의시설도 키워드에 없으면 넣어줘야 함
        // (만약 Stores 객체 안에 카테고리 정보가 있다면 여기서 indexKeyword 호출)
    }

    // 단일 키워드 저장 메소드
    public void indexKeyword(String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            String trimmed = keyword.trim();
            // 검색용 Key에만 추가 (0점). 랭킹용은 검색될 때 올라가므로 굳이 추가 안 해도 됨.
            redisTemplate.opsForZSet().add(SEARCH_KEY, trimmed, 0);
        }
    }

    // 키워드 삭제
    public void removeKeyword(String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            String trimmed = keyword.trim();
            redisTemplate.opsForZSet().remove(SEARCH_KEY, trimmed);
            // 랭킹 데이터는 남겨둘지 지울지 정책에 따라 결정 (여기선 둠)
        }
    }

    // -----------------------------------------------

    // 검색 완료 시 점수 증가 (랭킹용 Key만 건드림)
    public void incrementKeywordScore(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            // ★ 중요: RANK_KEY의 점수만 올립니다! SEARCH_KEY는 건드리지 마세요.
            redisTemplate.opsForZSet().incrementScore(RANK_KEY, keyword.trim(), 1);
        }
    }
}
