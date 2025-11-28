package com.reserve.illoomung.application.admin.amenity;

import com.reserve.illoomung.core.util.autocomplete.application.AutocompleteService;
import com.reserve.illoomung.domain.entity.Amenity;
import com.reserve.illoomung.domain.entity.enums.Status;
import com.reserve.illoomung.domain.repository.AmenityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmenityService {

    private final AmenityRepository amenityRepository;
    private final AutocompleteService autocompleteService;

    @Transactional
    public void addAmenity(String keyword) {

        Integer amenityCount = amenityRepository.findMaxSortOrder() + 1;

        Amenity amenity = Amenity.builder()
                .amenityName(keyword)
                .sortOrder(amenityCount)
                .status(Status.ACTIVE)
                .build();

        amenityRepository.save(amenity);
        autocompleteService.indexKeyword(keyword);
    }

    public Map<Long ,String> searchAmenity() {
        List<Amenity> amenities = amenityRepository.findAll();
        return amenities.stream().collect(Collectors.toMap(
                Amenity::getAmenityId,
                Amenity::getAmenityName
        ));
    }

    public void patchAmenity(Long id, String amenityName) {
        Amenity amenity = amenityRepository.findAllByAmenityId(id).orElseThrow(() -> new RuntimeException("해당 편의시설을 찾을 수 없습니다."));
        amenity.setAmenityName(amenityName);
    }

    public void deleteAmenity(Long id) {
        Amenity amenity = amenityRepository.findAllByAmenityId(id).orElseThrow(() -> new RuntimeException("해당 편의시설을 찾을 수 없습니다."));
        amenity.setStatus(Status.INACTIVE);
    }

    public void restoreAmenity(Long id) {
        Amenity amenity = amenityRepository.findAllByAmenityId(id).orElseThrow(() -> new RuntimeException("해당 편의시설을 찾을 수 없습니다."));
        amenity.setStatus(Status.ACTIVE);
    }

}
