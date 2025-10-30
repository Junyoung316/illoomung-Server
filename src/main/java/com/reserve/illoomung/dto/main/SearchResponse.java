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
public class SearchResponse {
    private Long StoreId;
    private String imgUrl;
    private String storeName;
    private String addr;
    private String isOpen;
    private List<String> amenities;

}

//    {
//        "storeId": 1,
//        "img": "https://wwww.----"
//        "sotreName": "본펫 플래닛",
//        "addr": "경기도 oo시 oo동 100",
//        "open": "09:00"
//        "close": "18:00"
//        "amenities": [
//            "예약",
//            "반려동물 동반",
//            "무선 인터넷",
//            "방문접수/출장",
//            "간편결제",
//            "주차"
//        ]
//    },