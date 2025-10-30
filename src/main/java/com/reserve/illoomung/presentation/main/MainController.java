package com.reserve.illoomung.presentation.main;

import com.reserve.illoomung.application.main.MainService;
import com.reserve.illoomung.core.dto.MainResponse;
import com.reserve.illoomung.dto.main.MainPageResponse;
import com.reserve.illoomung.dto.main.SearchResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
@Slf4j
public class MainController {

    private final MainService mainService;

    @GetMapping("/item") // 메인 페이지 데이터 전송, 캐러셀 7개
    public ResponseEntity<MainResponse<List<MainPageResponse>>> home() {
        List<MainPageResponse> data = mainService.mainInit();
        return ResponseEntity.ok(MainResponse.success(data));
    }

    // 상세 페이지 정보
    @PostMapping("/search")
    public ResponseEntity<MainResponse<List<SearchResponse>>> search(@RequestParam(name = "search", required = false) String item) {
        List<SearchResponse> data = mainService.searchItem(item);
        return ResponseEntity.ok(MainResponse.success(data));
    }

}
 /*
     {
			"storeId": 1,
			"sotreName": "본펫 플래닛",
			"addrDepth": "oo동",
			"isOpen": "영업 중"
			"amenities": [
				"예약",
	      "반려동물 동반",
	      "무선 인터넷",
	      "방문접수/출장",
	      "간편결제",
	      "주차"
			]
		}
 */