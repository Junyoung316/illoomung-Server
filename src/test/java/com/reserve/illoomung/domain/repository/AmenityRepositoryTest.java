package com.reserve.illoomung.domain.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reserve.illoomung.domain.entity.Amenity;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class AmenityRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(AmenityRepositoryTest.class);

    @Autowired
    private AmenityRepository amenityRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void saveAmenity() throws Exception {
        if(amenityRepository.count() == 0) {
            log.info("데이터베이스 편의시설 초기 데이터 저장");

            ClassPathResource resource = new ClassPathResource("AmenityList.json");
            InputStream inputStream = resource.getInputStream();

            List<String> amenityNames = objectMapper.readValue(inputStream, new TypeReference<>() {});

            List<Amenity> amenities = IntStream.range(0, amenityNames.size())
                            .mapToObj(i -> {
                                String name = amenityNames.get(i);
                                int sortOrder = i + 1;

                                Amenity amenity = new Amenity(name);
                                amenity.setSortOrder(sortOrder);
                                return amenity;
                            })
                            .collect(Collectors.toList());

            amenityRepository.saveAll(amenities);

            log.info("편의 시설 초기 데이터 저장 완료. 총 {}건",  amenities.size());
        } else {
            log.info("편의 시설 데이터가 이미 존재, 초기 데이터 저장 건너뜀");
        }
    }
}