package com.reserve.illoomung.dto.business;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class StoreCreateRequest {
    private String storeName;
    private String description;
    private String phoneNumber;
    private String roadAddress;
    private String jibeonAddress;
    private Map<String, String> openingHours;
    private String homepageUrl;
    private String instagramUrl;
    private String mainImageUrl;
    private List<String> amenities;
    private String products;
}
