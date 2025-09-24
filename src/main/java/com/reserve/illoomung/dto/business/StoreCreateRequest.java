package com.reserve.illoomung.dto.business;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class StoreCreateRequest {
    private String storeName;
    private String description;
    private String phoneNumber;
    private String roadAddress; // 도로명 주소
    private String jibeonAddress; // 지번 주소
    private String addressDetails; // 상세 주소
    private String bcode; // 지역 코드
    private Map<String, String> openingHours;
    private String homepageUrl;
    private String instagramUrl;
    private String mainImageUrl;
    private List<String> amenities;
    private String products;
}
