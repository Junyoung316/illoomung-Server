package com.reserve.illoomung.domain.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reserve.illoomung.domain.entity.StoreCategory;
import com.reserve.illoomung.domain.entity.enums.Status;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StoreCategoryRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(StoreCategoryRepositoryTest.class);

    @Autowired
    private StoreCategoryRepository storeCategoryRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // ('카페/놀이터'), ('운동장'), ('호텔'), ('미용'), ('병원'), ('유치원');

    @Test
    public void saveCategory() throws Exception {
        if(storeCategoryRepository.count() == 0) {
            log.info("스토어 카테고리 초기 데이터 저장");

            ClassPathResource resource = new ClassPathResource("storeCategory.json");
            InputStream inputStream = resource.getInputStream();

            List<String> categoryName = objectMapper.readValue(inputStream, new TypeReference<>() {});

            List<StoreCategory> category = IntStream.range(0, categoryName.size())
                    .mapToObj(i -> {
                        String name = categoryName.get(i);
                        int sortOrder = i + 1;

                        StoreCategory storeCategory = new StoreCategory(name);
                        storeCategory.setSortOrder(sortOrder);
                        storeCategory.setStatus(Status.ACTIVE);
                        return storeCategory;
                    })
                    .collect(Collectors.toList());

            storeCategoryRepository.saveAll(category);

            log.info("스토어 카테고리 초기 데이터 자장 완료. 총 {}건", category.size());
        } else {
            log.info("스토어 카테고리 이미 존재, 초기 데이터 저장 건너뜀");
        }
    }

}