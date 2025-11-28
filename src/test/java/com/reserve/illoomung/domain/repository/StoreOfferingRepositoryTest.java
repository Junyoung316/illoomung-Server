package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreOffering;
import com.reserve.illoomung.domain.entity.Stores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class StoreOfferingRepositoryTest {
    @Autowired
    private StoreOfferingRepository storeOfferingRepository;

    @Autowired
    private StoresRepository storesRepository;

    @Test
    void saveProductStoreAll() {
        int count = storesRepository.findAll().size();
        List<Stores> storesList = storesRepository.findAll();

        for (int i = 0; i < count; i++) {
            StoreOffering storeOffering = StoreOffering.builder()
                    .store(storesList.get(i))
                    .offeringName("Test")
                    .description("Test description")
                    .price("100")
                    .build();

            storeOfferingRepository.save(storeOffering);
        }
    }

}