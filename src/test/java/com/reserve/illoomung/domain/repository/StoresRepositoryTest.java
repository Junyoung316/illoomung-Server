package com.reserve.illoomung.domain.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reserve.illoomung.application.business.BusinessService;
import com.reserve.illoomung.dto.business.StoreCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
class StoresRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(StoresRepositoryTest.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private StoresRepository storesRepository;

    @BeforeEach
//    @Test
    void setUp() { // 초기 세팅
        storesRepository.deleteAll();
    }

    @Test
    void saveStores() throws Exception {
        ClassPathResource resource = new ClassPathResource("store.json");
        File jsonFile = resource.getFile();
        List<StoreCreateRequest> storeCreateRequests = objectMapper.readValue(jsonFile, new TypeReference<>() {});

        for(StoreCreateRequest storeCreateRequest : storeCreateRequests){
            log.info("Creating store: {}", storeCreateRequest.getStoreName());
            businessService.createTestStore(storeCreateRequest);
        }
    }

}