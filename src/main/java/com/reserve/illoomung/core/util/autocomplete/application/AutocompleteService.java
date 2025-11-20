package com.reserve.illoomung.core.util.autocomplete.application;

import com.reserve.illoomung.domain.repository.StoreOfferingRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;


import java.util.*;

@Service
@RequiredArgsConstructor
public class AutocompleteService {

    private final StringRedisTemplate redisTemplate;
    private final StoreOfferingRepository storeOfferingRepository;

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
        List<String> keywords = Arrays.asList(
                "강아지 미용 전체",
                "강아지 목욕 소형견",
                "고양이 스케일링",
                "고양이 목욕",
                "강아지 호텔링 1박"
        );

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



    // -----------------------------------------------

    // 검색 완료 시 점수 증가
    public void incrementKeywordScore(String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            redisTemplate.opsForZSet().incrementScore(KEYWORD_KEY, keyword.trim(), 1);
        }
    }
}
