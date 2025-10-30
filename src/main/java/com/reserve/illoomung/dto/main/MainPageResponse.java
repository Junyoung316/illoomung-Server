package com.reserve.illoomung.dto.main;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainPageResponse {
    private Long StoreId;
    private String imgUrl;
    private String storeName;
    private String addrDepth;
    private String isOpen;
    private List<String> amenities;
}

/*
 {
			"storeId": 1,
			"imgUrl": "----.jpg"
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
