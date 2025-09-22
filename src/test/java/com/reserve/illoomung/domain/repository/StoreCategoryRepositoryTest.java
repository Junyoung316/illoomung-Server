package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.StoreCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StoreCategoryRepositoryTest {

    @Autowired
    private StoreCategoryRepository repository;

    // ('카페/놀이터'), ('운동장'), ('호텔'), ('미용'), ('병원'), ('유치원');

    @Test
    public void addCategory() {
        StoreCategory storeCategory1 = new StoreCategory();
        storeCategory1.setCategoryName("카페/놀이터");
        repository.save(storeCategory1);

        StoreCategory storeCategory2 = new StoreCategory();
        storeCategory2.setCategoryName("운동장");
        repository.save(storeCategory2);

        StoreCategory storeCategory3 = new StoreCategory();
        storeCategory3.setCategoryName("호텔");
        repository.save(storeCategory3);

        StoreCategory storeCategory4 = new StoreCategory();
        storeCategory4.setCategoryName("미용");
        repository.save(storeCategory4);

        StoreCategory storeCategory5 = new StoreCategory();
        storeCategory5.setCategoryName("병원");
        repository.save(storeCategory5);

        StoreCategory storeCategory6 = new StoreCategory();
        storeCategory6.setCategoryName("유치원");
        repository.save(storeCategory6);
    }

}