package com.reserve.illoomung.application.es;

import com.reserve.illoomung.domain.entity.StoreImage;
import com.reserve.illoomung.domain.entity.StoreOperatingHours;
import com.reserve.illoomung.domain.entity.Stores;
import com.reserve.illoomung.domain.entity.es.StoreDocument;

import java.util.List;

public interface StoreSearchService {
    void syncStore(Stores store, StoreImage storeImage, List<StoreDocument.OperatingHourDto> isOpen, List<String> amenities); // 스토어 저장 및 수정 동기화
    List<StoreDocument> search(String query); // 검색
    List<StoreDocument> searchFromIndex(String query);
    void deleteStore(Long storeId); // 스토어 정보 es에서 제거
}
