package com.reserve.illoomung.application.main;

import com.reserve.illoomung.dto.main.MainPageResponse;
import com.reserve.illoomung.dto.main.SearchResponse;

import java.util.List;

public interface MainService {
    List<MainPageResponse> mainInit();
    List<SearchResponse> searchItem(String item);
}
