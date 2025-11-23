package com.reserve.illoomung.domain.entity.es;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.elasticsearch.annotations.*;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@Document(indexName = "stores")
@Setting(settingPath = "/es/es-settings.json")
@Mapping(mappingPath = "/es/es-mappings.json")
public class StoreDocument implements Serializable { // 캐싱을 위해 implements Serializable

    @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "korean_analyzer")
    private String name;       // 업체명 (예: 일루멍)

    @Field(type = FieldType.Text, analyzer = "korean_analyzer")
    private String fullAddress; // 통합 검색용 (예: 경기 김포시 구래동)

    // --- 결과 반환용 (검색엔 안씀) ---
    @Field(type = FieldType.Keyword) private String province;
    @Field(type = FieldType.Keyword) private String city;
    @Field(type = FieldType.Keyword) private String district;

    private String addr;       // 보여주기용 주소 (복호화된 값)
    private String imgUrl;  // 썸네일

    @Field(type = FieldType.Nested)
    private List<OperatingHourDto> operatingHours;

    @Transient
    private boolean openNow;       // 영업 중 여부

    private List<String> amenities; // 편의시설 리스트

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OperatingHourDto {
        private int dayOfWeek; // 1:월 ~ 7:일
        private String openTime;  // "09:00" (HH:mm 형식 권장)
        private String closeTime; // "22:00"
        private boolean isHoliday; // 휴무 여부
    }
}
