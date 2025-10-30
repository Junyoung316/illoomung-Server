package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.Stores;

import java.util.List;

// 💡 1. 우리가 만들 메서드를 정의할 인터페이스
public interface StoresRepositoryCustom {
    List<Stores> searchStores(String searchItem);
}