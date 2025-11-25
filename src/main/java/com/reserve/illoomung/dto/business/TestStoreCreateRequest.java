package com.reserve.illoomung.dto.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.reserve.illoomung.core.config.json.OpeningHoursDeserializer;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TestStoreCreateRequest {
    private String category;
    private String storeName;
    private String description;
    private String phoneNumber;
    private String address; // 주소
    private String addressDetails; // 상세 주소
    private String bcode; // 지역 코드

    @JsonDeserialize(using = OpeningHoursDeserializer.class)
    private Map<String, OperatingInfo> openingHours;

    private String homepageUrl;
    private String instagramUrl;
    private String mainImageUrl;

    private List<String> amenities;
    private String products;
}
