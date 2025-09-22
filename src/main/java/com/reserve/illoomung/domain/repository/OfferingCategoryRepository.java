package com.reserve.illoomung.domain.repository;

import com.reserve.illoomung.domain.entity.OfferingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferingCategoryRepository extends JpaRepository<OfferingCategory, Long> {
}
