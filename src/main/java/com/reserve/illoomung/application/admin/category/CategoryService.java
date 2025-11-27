package com.reserve.illoomung.application.admin.category;

import com.reserve.illoomung.core.util.autocomplete.application.AutocompleteService;
import com.reserve.illoomung.domain.entity.Amenity;
import com.reserve.illoomung.domain.entity.StoreCategory;
import com.reserve.illoomung.domain.entity.enums.Status;
import com.reserve.illoomung.domain.repository.StoreCategoryRepository;
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
public class CategoryService {

    private final StoreCategoryRepository storeCategoryRepository;
    private final AutocompleteService autocompleteService;

    @Transactional
    public void addCategory(String category) {

        Integer categoryCount = storeCategoryRepository.findMaxSortOrder() + 1;

        StoreCategory storeCategory = StoreCategory.builder()
                .categoryName(category)
                .sortOrder(categoryCount)
                .build();

        storeCategoryRepository.save(storeCategory);
    }

    public Map<Long, String> searchCategory() {
        List<StoreCategory> categories = storeCategoryRepository.findAll();
        return categories.stream().collect(Collectors.toMap(
                StoreCategory::getCategoryId,
                StoreCategory::getCategoryName
        ));
    }

    public void patchCategory(Long id, String category) {
        StoreCategory storeCategory = storeCategoryRepository.findById(id).orElseThrow()
    }

    public void deleteCategory(Long id) {}

    public void restoreCategory(Long id) {}

}
