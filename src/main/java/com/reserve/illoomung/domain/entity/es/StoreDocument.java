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
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "stores")
@Setting(settingPath = "/es/es-settings.json") // 경로가 resources/es/ 폴더가 맞는지 확인하세요
@Mapping(mappingPath = "/es/es-mappings.json")
public class StoreDocument implements Serializable {

    @Id
    private Long id;

    // [수정 1] 분석기 이름을 JSON 설정과 정확히 일치시킴
    @Field(type = FieldType.Text, analyzer = "korean_index_analyzer", searchAnalyzer = "korean_search_analyzer")
    private String name;

    // [수정 1] 분석기 이름 일치
    @Field(type = FieldType.Text, analyzer = "korean_index_analyzer", searchAnalyzer = "korean_search_analyzer")
    private String fullAddress;

    @Field(type = FieldType.Keyword)
    private String province;

    @Field(type = FieldType.Keyword)
    private String city;

    // [수정 2] 동의어 검색('구래리'->'구래동')을 위해 Keyword -> Text로 변경 및 분석기 적용
    @Field(type = FieldType.Text, analyzer = "korean_index_analyzer", searchAnalyzer = "korean_search_analyzer")
    private String district;

    private String addr;
    private String imgUrl;

    @Field(type = FieldType.Nested)
    private List<OperatingHourDto> operatingHours;

    // [수정 3] 매핑 파일과 일치시키기 위해 @Transient 제거 (저장 및 조회 가능하게 변경)
    @Field(type = FieldType.Boolean)
    private boolean openNow;

    @Field(type = FieldType.Keyword)
    private List<String> amenities;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OperatingHourDto {
        private int dayOfWeek;
        private String openTime;
        private String closeTime;
        private boolean isHoliday;
    }
}