package com.reserve.illoomung.presentation.admin.amenity;

import com.reserve.illoomung.application.admin.amenity.AmenityService;
import com.reserve.illoomung.core.dto.MainResponse;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/amenity")
@RequiredArgsConstructor
@Slf4j
public class AmenityController {

    private final AmenityService amenityService;

    @GetMapping
    public ResponseEntity<MainResponse<Map<Long, String>>> getAllAmenities() {
        return ResponseEntity.ok(MainResponse.success(amenityService.searchAmenity()));
    }

    @PostMapping("/add")
    public ResponseEntity<MainResponse<String>> addAmenity(@Param("key") String key) {
        amenityService.addAmenity(key);
        return ResponseEntity.ok(MainResponse.created());
    }

    @PostMapping("/patch/{amenityId}")
    public ResponseEntity<MainResponse<String>>  patchAmenity(@PathVariable("amenityId") Long amenityId, @Param("rename") String rename) {
        amenityService.patchAmenity(amenityId, rename);
        return ResponseEntity.ok(MainResponse.created());
    }

    @PostMapping("/delete/{amenityId}")
    public ResponseEntity<MainResponse<String>>  deleteAmenity(@PathVariable("amenityId") Long amenityId) {
        amenityService.deleteAmenity(amenityId);
        return ResponseEntity.ok(MainResponse.created());
    }

    @PostMapping("/restore/{amenityId}")
    public ResponseEntity<MainResponse<String>> restoreAmenity(@PathVariable("amenityId") Long amenityId) {
        amenityService.restoreAmenity(amenityId);
        return ResponseEntity.ok(MainResponse.created());
    }
}
