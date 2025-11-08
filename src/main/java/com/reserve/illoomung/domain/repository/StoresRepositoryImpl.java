package com.reserve.illoomung.domain.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.reserve.illoomung.domain.entity.Stores;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

import static com.reserve.illoomung.domain.entity.QStores.stores;

@Slf4j
@RequiredArgsConstructor // 👈 생성자 주입
public class StoresRepositoryImpl implements StoresRepositoryCustom {

    // 💡 4. 설정(Config)에서 Bean으로 등록한 JPAQueryFactory를 주입받음
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Stores> searchStores(String searchItem) {

        log.debug("searchStores [{}]", searchItem);

        // 💡 1. 키워드가 없으면 빈 리스트 반환 (혹은 전체 리스트)
        if (!StringUtils.hasText(searchItem)) {
            return Collections.emptyList();
            // 또는 queryFactory.selectFrom(store).fetch(); 등으로 전체 반환
        }

        // 💡 2. 키워드가 있으면
        return queryFactory
                .selectFrom(stores)
                .where(
                        // [검색 대상: WHERE] - '포함(contains)'으로 OR 검색
                        stores.storeName.contains(searchItem)
                                .or(stores.addrDepth1.contains(searchItem))
                                .or(stores.addrDepth2.contains(searchItem))
                                .or(stores.addrDepth3.contains(searchItem))
                )
                .orderBy(
                        // [정확도: ORDER BY] - '일치(eq)'하는 항목을 우선 정렬
                        priorityOrder(searchItem)
                )
                .fetch();
    }

    /**
     * 💡 3. [핵심] '일치(eq)' 조건에 가중치를 부여하는 정렬(OrderSpecifier) 생성
     */
    private OrderSpecifier<?> priorityOrder(String searchItem) {

        NumberExpression<Integer> cases = new CaseBuilder()
                // 1순위: '구(district)'가 정확히 일치 (가장 구체적인 주소 조건)
                .when(stores.addrDepth2.eq(searchItem)).then(1)
                // 2순위: '동(dong)'이 정확히 일치
                .when(stores.addrDepth3.eq(searchItem)).then(2)
                // 3순위: '시(city)'가 정확히 일치
                .when(stores.addrDepth1.eq(searchItem)).then(3)
                // 4순위: 가게 이름(name)이 '포함'
                .when(stores.storeName.contains(searchItem)).then(4)
                // 5순위: 그 외 (주소 '포함' 등)
                .otherwise(5);

        return cases.asc(); // 1순위(숫자가 낮은)가 가장 위로 오도록 오름차순 정렬
    }
}