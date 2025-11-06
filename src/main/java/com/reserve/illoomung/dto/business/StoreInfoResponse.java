package com.reserve.illoomung.dto.business;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreInfoResponse {
    private Long storeId;
    private String imgUrl;
    private String name;
    private String description;

    @Singular("amenity")
    private List<String> amenities;

    private String addr;
    private String addrDetail;
    private String x;
    private String y;

    @Singular("closingHour")
    private List<openCloseHours> openCloseHours;

    @Singular("product")
    private List<products> products;

    private seller seller;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class openCloseHours{
        private String dayOfWeek;
        private boolean isOpening;
        private String openingHour;
        private String closingHour;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class products {
        private Long productsId;
        private String productName;
        private String productDescription;
        private String productPrice;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class seller {
        private Long sellerId;
        private String sellerName;
        private String sellerEmail;
    }
}

/*
스토어 id
이미지
가게 이름
설명
편의시설
주소
주소 상세
위도
경도
영업시간
상품 및 가격
판매자 정보

 */