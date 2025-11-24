package com.reserve.illoomung.domain.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reserve.illoomung.application.business.BusinessService;
import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.repository.AccountRepository;
import com.reserve.illoomung.dto.business.StoreCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
//@Transactional
class StoresRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(StoresRepositoryTest.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BusinessService businessService;

    @Autowired
    private StoresRepository storesRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
//    @Test
    void setUp() { // 초기 세팅
        storesRepository.deleteAll();
    }

//    @Test
//    void saveStores() throws Exception {
//        ClassPathResource resource = new ClassPathResource("store.json");
//        File jsonFile = resource.getFile();
//        List<StoreCreateRequest> storeCreateRequests = objectMapper.readValue(jsonFile, new TypeReference<>() {});
//        Account owner = accountRepository.findById(1L).orElseThrow();
//        log.info("authenticated account: {}, {}", owner.getAccountId(), owner.getRole());
//        for(StoreCreateRequest storeCreateRequest : storeCreateRequests){
//            log.info("Creating store: {}", storeCreateRequest.getStoreName());
//            businessService.createStore(storeCreateRequest);
//        }
//    }

//    @Test
//    void saveStores() throws Exception {
//        ClassPathResource resource = new ClassPathResource("store.json");
//        File jsonFile = resource.getFile();
//        List<StoreCreateRequest> storeCreateRequests = objectMapper.readValue(jsonFile, new TypeReference<>() {});
//
//        Account owner = accountRepository.findById(1L).orElseThrow();
//
//        for(StoreCreateRequest storeCreateRequest : storeCreateRequests){
//            log.info("Creating store: {}", storeCreateRequest.getStoreName());
//            businessService.createTestStore(storeCreateRequest, owner);
//        }
//
//        // 첫 번째 샘플을 JSON으로 변환 후 POST API 호출 테스트
//        String jsonString = objectMapper.writeValueAsString(storeCreateRequests.get(1));
//
//        mockMvc.perform(post("/business/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonString)
//                        .header("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicm9sZSI6IkFETUlOIiwidG9rZW5UeXBlIjoiYWNjZXNzIiwiaWF0IjoxNzU5Mjg1ODQ2LCJleHAiOjE3NTkyODc2NDZ9.4Xy_38zFsJ6o7_eq2Pxase3TNmVMHK5u88lMtaDvlpwYR80pKJE67PnKZfzDLP1uvyruTORz7HkiKxLAN3NhZg"))
//                .andExpect(status().isOk());
//    }


}