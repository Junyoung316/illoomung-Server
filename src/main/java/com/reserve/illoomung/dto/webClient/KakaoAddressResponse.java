package com.reserve.illoomung.dto.webClient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAddressResponse {

    private List<Document> documents;
    private Meta meta;

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Document {
        private Address address;

        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("address_type")
        private String addressType;

        @JsonProperty("road_address")
        private RoadAddress roadAddress;

        private String x;
        private String y;

    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("b_code")
        private String bCode;

        @JsonProperty("h_code")
        private String hCode;

        @JsonProperty("main_address_no")
        private String mainAddressNo;

        @JsonProperty("mountain_yn")
        private String mountainYn;

        @JsonProperty("region_1depth_name")
        private String region1depthName;

        @JsonProperty("region_2depth_name")
        private String region2depthName;

        @JsonProperty("region_3depth_h_name")
        private String region3depthHName;

        @JsonProperty("region_3depth_name")
        private String region3depthName;

        @JsonProperty("sub_address_no")
        private String subAddressNo;

        private String x; // 경도
        private String y; // 위도

        // Getters and setters
        // ...
    }

    @Setter
    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RoadAddress {
        @JsonProperty("address_name")
        private String addressName;

        @JsonProperty("building_name")
        private String buildingName;

        @JsonProperty("main_building_no")
        private String mainBuildingNo;

        @JsonProperty("region_1depth_name")
        private String region1depthName;

        @JsonProperty("region_2depth_name")
        private String region2depthName;

        @JsonProperty("region_3depth_name")
        private String region3depthName;

        @JsonProperty("road_name")
        private String roadName;

        @JsonProperty("sub_building_no")
        private String subBuildingNo;

        @JsonProperty("underground_yn")
        private String undergroundYn;

        private String x;
        private String y;

        @JsonProperty("zone_no")
        private String zoneNo;

        // Getters and setters
        // ...
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Meta {
        @JsonProperty("is_end")
        private boolean isEnd;

        @JsonProperty("pageable_count")
        private int pageableCount;

        @JsonProperty("total_count")
        private int totalCount;

        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }

        public int getPageableCount() {
            return pageableCount;
        }

        public void setPageableCount(int pageableCount) {
            this.pageableCount = pageableCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }
    }
}
