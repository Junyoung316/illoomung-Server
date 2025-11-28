package com.reserve.illoomung.presentation.admin.category;

import com.reserve.illoomung.application.admin.category.CategoryService;
import com.reserve.illoomung.core.dto.MainResponse;
import io.lettuce.core.dynamic.annotation.Param;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<MainResponse<Map<Long, String>>> getAllAmenities() {
        return ResponseEntity.ok(MainResponse.success(categoryService.searchCategory()));
    }

    @PostMapping("/add")
    public ResponseEntity<MainResponse<String>> addAmenity(@Param("key") String key) {
        categoryService.addCategory(key);
        return ResponseEntity.ok(MainResponse.created());
    }

    @PostMapping("/patch/{categoryId}")
    public ResponseEntity<MainResponse<String>>  patchAmenity(@PathVariable("categoryId") Long categoryId, @Param("rename") String rename) {
        categoryService.patchCategory(categoryId, rename);
        return ResponseEntity.ok(MainResponse.created());
    }

    @PostMapping("/delete/{categoryId}")
    public ResponseEntity<MainResponse<String>>  deleteAmenity(@PathVariable("categoryId") Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(MainResponse.created());
    }

    @PostMapping("/restore/{categoryId}")
    public ResponseEntity<MainResponse<String>> restoreAmenity(@PathVariable("categoryId") Long categoryId) {
        categoryService.restoreCategory(categoryId);
        return ResponseEntity.ok(MainResponse.created());
    }
}
